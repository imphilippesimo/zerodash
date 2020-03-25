package com.zerofiltre.zerodash.model;

import lombok.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ZDUser extends RootEntity implements Serializable {
    private String email;
    private String phoneNumber;
    private String role;
    private String password;
    private Boolean activated;
    private String activationKey;


}
