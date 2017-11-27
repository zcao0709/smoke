package com.zhongyun.smoke.model.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by caozhennan on 2017/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth {
    private Account account;

    public Auth(String user, String pwd) {
        this();
        account = new Account(user, pwd);
    }

    public boolean accepted() {
        return account.isAccept();
    }

    public Auth() {
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Account {
        private String user;
        private String customer;
        private String pwd;
        private boolean DwnData;
        private boolean accept;
        private boolean serverAccept;
        private String serverUser;
        private String serverPwd;
        private String serverDB;

        public Account(String user, String pwd) {
            this.user = user;
            this.pwd = pwd;
        }

        public Account() {
        }

        public void setUser(String user) {
            this.user = user;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public void setDwnData(boolean dwnData) {
            DwnData = dwnData;
        }

        public void setAccept(boolean accept) {
            this.accept = accept;
        }

        public void setServerAccept(boolean serverAccept) {
            this.serverAccept = serverAccept;
        }

        public void setServerUser(String serverUser) {
            this.serverUser = serverUser;
        }

        public void setServerPwd(String serverPwd) {
            this.serverPwd = serverPwd;
        }

        public void setServerDB(String serverDB) {
            this.serverDB = serverDB;
        }

        public String getUser() {
            return user;
        }

        public String getCustomer() {
            return customer;
        }

        public String getPwd() {
            return pwd;
        }

        public boolean isDwnData() {
            return DwnData;
        }

        public boolean isAccept() {
            return accept;
        }

        public boolean isServerAccept() {
            return serverAccept;
        }

        public String getServerUser() {
            return serverUser;
        }

        public String getServerPwd() {
            return serverPwd;
        }

        public String getServerDB() {
            return serverDB;
        }
    }
}
