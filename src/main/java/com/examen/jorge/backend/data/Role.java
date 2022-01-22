package com.examen.jorge.backend.data;

public class Role {
	public static final String ADMIN = "admin";
	public static final String USER = "user";
    //USER("user"), ADMIN("admin");

    private String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static String[] getAllRoles() {
		return new String[] {ADMIN, USER };
	}
}
