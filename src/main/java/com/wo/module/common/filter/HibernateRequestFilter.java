package com.wo.module.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


//assume this filter get called after authorization filter.
public class HibernateRequestFilter implements Filter {
    //private static Logger log = Logger.getLogger(HibernateRequestFilter.class);
	//private FilterConfig filterConfig;
	
	@Autowired()
    @Qualifier("sessionFactory")
	private SessionFactory sessionFactory;
	
	@Autowired
	private HttpServletRequest servletRequest;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void destroy()
	{
		System.out.println("Destroying filter...");
	}

	@SuppressWarnings("deprecation")
	public void doFilter(ServletRequest request, 
						 ServletResponse response,
						 FilterChain chain) throws IOException, ServletException
	{
		HttpSession httpSession = ((HttpServletRequest) request).getSession(false);
		
        try 
        {
        	if(servletRequest == null || servletRequest.getSession().getAttribute("userLoginSession") == null){
        	if (httpSession != null && sessionFactory!=null) 
        	{
	            System.out.println("Starting a database transaction");
	            sessionFactory.getCurrentSession().setFlushMode(FlushMode.MANUAL);
	            sessionFactory.getCurrentSession().beginTransaction();
	
	            // Call the next filter (continue request processing)
	            chain.doFilter(request, response);
	
	            // Commit and cleanup
	            System.out.println("Committing the database transaction");
	        	sessionFactory.getCurrentSession().getTransaction().commit();
        	} 
        	else 
        	{
        		System.out.println("Session Time Out!");
        		request.getRequestDispatcher("/pages/home.faces").forward(request, response);
        	}
        	}else{
        		request.getRequestDispatcher("/pages/home.faces").forward(request, response);
        		
        	}
        } 
        catch (StaleObjectStateException staleEx) 
        {
            System.out.println("This interceptor does not implement optimistic concurrency control!");
            System.out.println("Your application will not work until you add compensation actions!");
            // Rollback, close everything, possibly compensate for any permanent changes
            // during the conversation, and finally restart business conversation. Maybe
            // give the user of the application a chance to merge some of his work with
            // fresh data... what you do here depends on your applications design.
            throw staleEx;
        } 
        catch (Throwable ex) 
        {
            // Rollback only
            ex.printStackTrace();
            try 
            {
                if (sessionFactory.getCurrentSession().getTransaction().isActive()) 
                {
                    System.out.println("Trying to rollback database transaction after exception");
                    sessionFactory.getCurrentSession().getTransaction().rollback();
                }
            } 
            catch (Throwable rbEx) 
            {
                System.out.println("Could not rollback transaction after exception!"+rbEx.getMessage());
            }

            // Let others handle it... maybe another interceptor for exceptions?
            throw new ServletException(ex);
        }
	}

	public void init(FilterConfig arg0) throws ServletException
	{
		System.out.println("Initializing filter...");
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	
	

	/*@Override
	public void destroy() {
		//System.out.println("Destroying filter...");
	}

	@Override
	public void doFilter(ServletRequest request, 
			 ServletResponse response,
			 FilterChain chain) throws IOException, ServletException {
		//SessionFactory sessionFactory = null;
		
		System.out.println("doFilter Hibernate Util");
		
        ServletContext context = filterConfig.getServletContext();
        SessionFactoryConfigBased configSessionFactory = 
        	(SessionFactoryConfigBased) context.getAttribute("configSessionFactory");

        if (configSessionFactory == null) {
        	configSessionFactory = new SessionFactoryConfigBased();
        	context.setAttribute("configSessionFactory", configSessionFactory);
        }

        try 
        {
        	//System.out.println("Starting a database transaction");           
            
            //sessionFactory = configSessionFactory.getSessionFactory();
            sessionFactory.getCurrentSession().setFlushMode(FlushMode.MANUAL);
            sessionFactory.getCurrentSession().beginTransaction();
            
          //Check Multipart Request [ERNEST]
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            //HttpServletResponse responseServlet = (HttpServletResponse) response;
			boolean isMultipartContent = ServletFileUpload.isMultipartContent(httpRequest);
            System.out.println("isMultipartContent:"+isMultipartContent);
            if (!isMultipartContent) {
            	String ae = httpRequest.getHeader("accept-encoding");
    			if (ae != null && ae.indexOf("gzip") != -1) {
    				System.out.println("GZIP supported, compressing.");
    				GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(
    						responseServlet);
    				chain.doFilter(request, wrappedResponse);
    				wrappedResponse.finishResponse();
    				return;
    			}
                chain.doFilter(request, response);
            } else {
            	//uncomment jika conven
            	try {
                    DiskFileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    upload.setHeaderEncoding("UTF-8");
                    upload.setSizeMax(-1);

                    List<FileItem> items = upload.parseRequest(httpRequest);
                    final Map<String, String[]> parameterMap = new HashMap<String, String[]>();

                    for (FileItem item : items) {
                        if (item.isFormField()) {
                            processFormField(item, parameterMap);
                        } else {
                            processFileField(item, httpRequest);
                        }
                    }

                    chain.doFilter(new HttpServletRequestWrapper(httpRequest) {
                            public Map<String, String[]> getParameterMap() {
                                return parameterMap;
                            }

                            public String[] getParameterValues(String name) {
                                return (String[])parameterMap.get(name);
                            }

                            public String getParameter(String name) {
                                String[] params = getParameterValues(name);
                                if (params == null) {
                                    return null;
                                }
                                return params[0];
                            }

                            public Enumeration<String> getParameterNames() {
                                return Collections.enumeration(parameterMap.keySet());
                            }
                        }, response);
                } catch (Exception ex) {
                    ServletException servletException = new ServletException();
                    servletException.initCause(ex);
                    throw servletException;
                }
            	//end uncomment jika conven
            	
            	//chain.doFilter(request, response);
            }

            // Call the next filter (continue request processing)
            //chain.doFilter(request, response);

            // Commit and cleanup
            //System.out.println("Committing the database transaction");
            if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
            	sessionFactory.getCurrentSession().getTransaction().commit();     
            }
        }
        catch (StaleObjectStateException staleEx) 
        {
            //System.out.println("This interceptor does not implement optimistic concurrency control!");
            //System.out.println("Your application will not work until you add compensation actions!");
            throw staleEx;
        } 
        catch (Throwable ex) 
        {
            // Rollback only
            ex.printStackTrace();
            try 
            {
                if (sessionFactory != null && sessionFactory.getCurrentSession().getTransaction().isActive()) 
                {
                    //System.out.println("Trying to rollback database transaction after exception");
                    sessionFactory.getCurrentSession().getTransaction().rollback();
                }
            } 
            catch (Throwable rbEx) 
            {
                //System.out.println("Could not rollback transaction after exception!", rbEx);
            }

            throw new ServletException(ex);
        }		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//System.out.println("Initializing filter...");		
		this.filterConfig = filterConfig;
	}
	
	//Multipart Request [ERNEST]
	 private void processFormField(FileItem formField, Map<String, String[]> parameterMap) {
		//System.out.println("formField.getFieldName():"+formField.getFieldName());
		//System.out.println("formField.getString():"+formField.getString());
		String name = formField.getFieldName();
        String value = formField.getString();
        String[] values = parameterMap.get(name);

        if (values == null) {
            parameterMap.put(name, new String[] { value });
        } else {
            int length = values.length;
            String[] newValues = new String[length + 1];
            System.arraycopy(values, 0, newValues, 0, length);
            newValues[length] = value;
            parameterMap.put(name, newValues);
        }
    }

    private void processFileField(FileItem fileField, HttpServletRequest request) {
    	System.out.println("fileField.getFieldName():"+fileField.getFieldName());
    	System.out.println("fileField.getName():"+fileField.getName());
        if (request.getAttribute(fileField.getFieldName()) == null) {
            List<FileItem> fileFields = new ArrayList<FileItem>(0);
            fileFields.add(fileField);
            request.setAttribute(fileField.getFieldName(), fileFields);
        } else {
            List<FileItem> fileFields = (List<FileItem>)request.getAttribute(fileField.getFieldName());
            fileFields.add(fileField);
        }
    }
*/}
