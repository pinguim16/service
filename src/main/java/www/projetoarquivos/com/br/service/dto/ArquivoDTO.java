package www.projetoarquivos.com.br.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Arquivo entity.
 */
public class ArquivoDTO implements Serializable {

	private static final long serialVersionUID = 3991408255472500847L;

	private Long id;

    private String nome;

    private String linkDownload;

    private Double tamanho;

    private String senha;

    private MegaDTO mega;

    private MegaDTO megaBackup;

    private AutorDTO autor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLinkDownload() {
        return linkDownload;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

    public Double getTamanho() {
        return tamanho;
    }

    public void setTamanho(Double tamanho) {
        this.tamanho = tamanho;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public MegaDTO getMega() {
		return mega;
	}

	public void setMega(MegaDTO mega) {
		this.mega = mega;
	}

	public MegaDTO getMegaBackup() {
		return megaBackup;
	}

	public void setMegaBackup(MegaDTO megaBackup) {
		this.megaBackup = megaBackup;
	}

	public AutorDTO getAutor() {
		return autor;
	}

	public void setAutor(AutorDTO autor) {
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

        ArquivoDTO arquivoDTO = (ArquivoDTO) o;
        if(arquivoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), arquivoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArquivoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", linkDownload='" + getLinkDownload() + "'" +
            ", tamanho='" + getTamanho() + "'" +
            ", senha='" + getSenha() + "'" +
            "}";
    }
}
