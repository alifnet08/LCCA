package com.wo.module.common.util;

public enum LdapEnum {
    /*IP_LDAP("10.225.16.180"),
    PORT_LDAP("389"),
    SECURITY_PRINCIPAL("development.dnroot.net"),
    CONTEXT_FACTORY("com.sun.jndi.ldap.LdapCtxFactory"),
    BASE_DN("CN=Users"),
    DOMAIN("DC=Development, DC=dnroot, DC=net")
    ;*/
	
	IP_LDAP("127.0.0.1"),
    PORT_LDAP("10388"),
    SECURITY_PRINCIPAL("example.com"),
    CONTEXT_FACTORY("com.sun.jndi.ldap.LdapCtxFactory"),
    BASE_DN("OU=Employees"),
    DOMAIN("DC=example, DC=com"),
    USER_NAME("hendra"),
    PASSWORD("h3ndr407"),
    ;

    private String message;
    
    LdapEnum(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
