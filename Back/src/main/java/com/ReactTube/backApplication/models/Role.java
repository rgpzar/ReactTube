package com.ReactTube.backApplication.models;

import java.util.Arrays;
import java.util.List;


public enum Role {
        USER(List.of(Permission.READ_AIRPORTS)),
        ADMIN(Arrays.asList(Permission.values()));

        private List<Permission> permissions;

        Role(List<Permission> permissions) {
            this.permissions = permissions;
        }

        public List<Permission> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<Permission> permissions) {
            this.permissions = permissions;
        }
}