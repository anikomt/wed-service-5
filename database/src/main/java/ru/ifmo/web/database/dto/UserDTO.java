package ru.ifmo.web.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class UserDTO implements Serializable {
    private String login;
    private String password;
    private String email;
    private Boolean gender = true; //True - man, false - woman
    private String registerDate;
}
