package de.brockhausag.jedichat.data.entities;


import de.brockhausag.jedichat.auth.UserRole;
import de.brockhausag.jedichat.data.enums.Fraction;
import de.brockhausag.jedichat.data.enums.Gender;
import de.brockhausag.jedichat.data.enums.Species;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @Getter @Setter
    @Column(unique=true)
    private String nickName;
    @Getter @Setter
    private String passwordHash;
    @Getter @Setter
    private UserRole role;
    @Getter @Setter @Lob
    private byte[] avatar;
    @Getter @Setter
    private Gender gender;
    @Getter @Setter
    private Fraction fraction;
    @Getter @Setter
    private Species species;
    @Getter @Setter
    private String bio;
}
