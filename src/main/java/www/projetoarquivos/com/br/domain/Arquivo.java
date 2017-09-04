package www.projetoarquivos.com.br.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Arquivo.
 */
@Entity
@Table(name = "arquivo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "link_download")
    private String linkDownload;

    @Column(name = "tamanho")
    private Double tamanho;

    @Column(name = "senha")
    private String senha;

    @ManyToOne
    private Mega mega;

    @ManyToOne
    private Mega megaBackup;

    @ManyToOne
    private Autor autor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Arquivo nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLinkDownload() {
        return linkDownload;
    }

    public Arquivo linkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
        return this;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

    public Double getTamanho() {
        return tamanho;
    }

    public Arquivo tamanho(Double tamanho) {
        this.tamanho = tamanho;
        return this;
    }

    public void setTamanho(Double tamanho) {
        this.tamanho = tamanho;
    }

    public String getSenha() {
        return senha;
    }

    public Arquivo senha(String senha) {
        this.senha = senha;
        return this;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Mega getMega() {
        return mega;
    }

    public Arquivo mega(Mega mega) {
        this.mega = mega;
        return this;
    }

    public void setMega(Mega mega) {
        this.mega = mega;
    }

    public Mega getMegaBackup() {
        return megaBackup;
    }

    public Arquivo megaBackup(Mega mega) {
        this.megaBackup = mega;
        return this;
    }

    public void setMegaBackup(Mega mega) {
        this.megaBackup = mega;
    }

    public Autor getAutor() {
        return autor;
    }

    public Arquivo autor(Autor autor) {
        this.autor = autor;
        return this;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Arquivo arquivo = (Arquivo) o;
        if (arquivo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), arquivo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Arquivo{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", linkDownload='" + getLinkDownload() + "'" +
            ", tamanho='" + getTamanho() + "'" +
            ", senha='" + getSenha() + "'" +
            "}";
    }
}
