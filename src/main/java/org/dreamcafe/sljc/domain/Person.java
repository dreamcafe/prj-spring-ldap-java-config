package org.dreamcafe.sljc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class Person implements Serializable {
    private String uid;
    private String fullName;
    private String surName;
}
