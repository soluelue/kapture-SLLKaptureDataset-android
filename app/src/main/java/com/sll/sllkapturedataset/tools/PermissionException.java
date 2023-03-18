package com.sll.sllkapturedataset.tools;

import java.nio.file.FileSystem;

public class PermissionException extends Exception{

    private String[] permissions = null;

    public PermissionException() {
        super();
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    public PermissionException(String ...permissions){
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
