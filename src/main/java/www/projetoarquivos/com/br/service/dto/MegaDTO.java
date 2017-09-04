package www.projetoarquivos.com.br.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Mega entity.
 */
public class MegaDTO implements Serializable {

    private Long id;

    private String nome;

    private String senha;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MegaDTO megaDTO = (MegaDTO) o;
        if(megaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), megaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MegaDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", senha='" + getSenha() + "'" +
            "}";
    }
}
