package me.brlw.bip.account;

import me.brlw.bip.utils.RandomStringGenerator;
import me.brlw.bip.redirection.Redirection;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by ww on 08.09.16.
 */

@Entity
@Table(name = "accounts")
public class Account implements Serializable
{
    public static final int MIN_ACCOUNT_ID_LENGTH = 3;
    public static final int MAX_ACCOUNT_ID_LENGTH = 25;

    public static final int MAX_HASHED_PASSWORD_LENGTH = 60;

    private Long recno;
    private int version;
    private String id;
    private String password;
    private List<Redirection> redirections = new ArrayList<>();

    public Account(@NotNull AccountDto accountDto)
    {
        this.id = accountDto.id;
        this.password = RandomStringGenerator.generatePassword();
    }

    public Account()
    {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recno")
    public Long getRecno() {
        return recno;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    @NaturalId
    @NotEmpty(message="AccountId can't be empty")
    @Size(min=MIN_ACCOUNT_ID_LENGTH,
            max=MAX_ACCOUNT_ID_LENGTH,
            message="Length of AccountId must be between {min} and {max}")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9]+$",
            message = "AccountId must contain only letters and digits, and first character must be letter")
    @Column(name = "id", unique = true, nullable = false, length = MAX_ACCOUNT_ID_LENGTH)
    public String getAccountId() {
        return id;
    }

    @NotEmpty(message="Password can't be empty")
    @Column(name = "password", nullable = false, length = MAX_HASHED_PASSWORD_LENGTH)
    public String getPassword() {
        return password;
    }

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Redirection> getRedirections()
    {
        return redirections;
    }

    public void setRedirections(List<Redirection> redirections) {
        this.redirections = redirections;
    }

    public void addRedirection(Redirection redirection)
    {
        redirections.add(redirection);
        redirection.setAccount(this);
    }

    public void removeRedirection(Redirection redirection)
    {
        redirections.remove(redirection);
        redirection.setAccount(null);
    }

    public void setRecno(Long recno) {
        this.recno = recno;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setAccountId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "recno=" + recno +
                ", version=" + version +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
