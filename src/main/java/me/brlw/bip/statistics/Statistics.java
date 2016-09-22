package me.brlw.bip.statistics;

import me.brlw.bip.redirection.Redirection;

import javax.persistence.*;

/**
 * Created by ww on 09.09.16.
 */

@Entity
@Table(name = "statistics")
public class Statistics
{
    private Long recno;
    private int version;
    private Redirection redirection;
    private Long numRedirects = 1L;

    public Statistics()
    {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recno")
    public Long getRecno() {
        return recno;
    }

    public void setRecno(Long recno) {
        this.recno = recno;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rdr_recno")
    public Redirection getRedirection() {
        return redirection;
    }

    public void setRedirection(Redirection redirection) {
        this.redirection = redirection;
    }

    @Column(name = "numredirects", nullable = false)
    public Long getNumRedirects() {
        return numRedirects;
    }

    public void setNumRedirects(Long numRedirects) {
        this.numRedirects = numRedirects;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "recno=" + recno +
                ", version=" + version +
                ", redirection=" + redirection +
                ", numRedirects=" + numRedirects +
                '}';
    }
}
