package com.example.RegisterLogin.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserCriteria implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String searchKey;
}
