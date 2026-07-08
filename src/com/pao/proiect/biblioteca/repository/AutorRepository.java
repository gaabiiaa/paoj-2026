package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Autor;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutorRepository implements Repository<Autor, String> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Autor entity) {
        String sql = "INSERT INTO autori (id, nume, email, nationalitate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getNationalitate());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea autorului", e);
        }
    }

    @Override
    public Optional<Autor> findById(String id) {
        String sql = "SELECT id, nume, email, nationalitate FROM autori WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea autorului", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Autor> findAll() {
        String sql = "SELECT id, nume, email, nationalitate FROM autori";
        List<Autor> autori = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                autori.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea autorilor", e);
        }
        return autori;
    }

    @Override
    public void update(Autor entity) {
        String sql = "UPDATE autori SET nume = ?, email = ?, nationalitate = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getNationalitate());
            statement.setString(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea autorului", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM autori WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea autorului", e);
        }
    }

    private Autor mapRow(ResultSet resultSet) throws SQLException {
        return new Autor(
                resultSet.getString("id"),
                resultSet.getString("nume"),
                resultSet.getString("email"),
                resultSet.getString("nationalitate")
        );
    }
}
