package br.com.fourbank.fourbank.adapter.out.persistence.mapper;

import br.com.fourbank.fourbank.adapter.out.persistence.data.usuario.UsuarioData;
import br.com.fourbank.fourbank.application.model.usuario.Usuario;

public final class UsuarioPersistenceMapper {

    private UsuarioPersistenceMapper() {
    }

    public static UsuarioData toData(Usuario usuario) {
        return new UsuarioData(
                usuario.getId(),
                usuario.getNome(),
                usuario.getDocumento(),
                usuario.getTipoPessoa(),
                usuario.getEmail(),
                usuario.getSenhaHash(),
                usuario.getRole(),
                usuario.getCriadoEm()
        );
    }

    public static Usuario toModel(UsuarioData entity) {
        return Usuario.restaurar(
                entity.getId(),
                entity.getNome(),
                entity.getDocumento(),
                entity.getTipoPessoa(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getRole(),
                entity.getCriadoEm()
        );
    }
}
