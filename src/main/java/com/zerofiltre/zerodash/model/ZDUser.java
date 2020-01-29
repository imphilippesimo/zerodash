package com.zerofiltre.zerodash.model;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
public class ZDUser extends RootEntity implements Serializable {
    private String email;
    private String phoneNumber;
    private String role;
    private String password;

}
