package com.wo.module.user.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wo.module.user.model.Division;
import com.wo.module.user.service.UserService;

@FacesConverter(value = "divisionConverter")
public class DivisionConverter implements Converter{
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value != null && value.trim().length() >0){
			try {
				return userService.findUsedDivisionByDivisionId(Long.parseLong(value));
				
				//return userService.getUserDivisionAsMap().get(Long.parseLong(value));
			}catch(NumberFormatException ex) {
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Division"));
			}
		}else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null) {
			return String.valueOf(((Division) value).getDivisionId());
		}else {
			return null;
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
