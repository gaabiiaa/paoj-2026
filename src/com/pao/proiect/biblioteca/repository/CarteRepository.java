package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Autor;
import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarteRepository implements Repository<Carte, String> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Carte entity) {
        String sql = "INSERT INTO carti (id, titlu, autor_id, categorie, disponibila, numar_imprumuturi) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getTitlu());
            statement.setString(3, entity.getAutor().getId());
            statement.setString(4, entity.getCategorie());
            statement.setBoolean(5, entity.isDisponibila());
            statement.setInt(6, entity.getNumarImprumuturi());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cartii", e);
        }
    }

    @Override
    public Optional<Carte> findById(String id) {
        String sql = "SELECT c.id, c.titlu, c.categorie, c.disponibila, c.numar_imprumuturi, " +
                "a.id AS autor_id, a.nume AS autor_nume, a.email AS autor_email, a.nationalitate " +
                "FROM carti c JOIN autori a ON c.autor_id = a.id WHERE c.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cartii", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Carte> findAll() {
        String sql = "SELECT c.id, c.titlu, c.categorie, c.disponibila, c.numar_imprumuturi, " +
                "a.id AS autor_id, a.nume AS autor_nume, a.email AS autor_email, a.nationalitate " +
                "FROM carti c JOIN autori a ON c.autor_id = a.id ORDER BY c.titlu";
        List<Carte> carti = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                carti.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cartilor", e);
        }
        return carti;
    }

    @Override
    public void update(Carte entity) {
        String sql = "UPDATE carti SET titlu = ?, autor_id = ?, categorie = ?, disponibila = ?, numar_imprumuturi = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getTitlu());
            statement.setString(2, entity.getAutor().getId());
            statement.setString(3, entity.getCategorie());
            statement.setBoolean(4, entity.isDisponibila());
            statement.setInt(5, entity.getNumarImprumuturi());
            statement.setString(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cartii", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM carti WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cartii", e);
        }
    }

    public List<String> findCartiCuAutori() {
        String sql = "SELECT c.titlu, a.nume AS autor_nume FROM carti c JOIN autori a ON c.autor_id = a.id ORDER BY c.titlu";
        List<String> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(resultSet.getString("titlu") + " - " + resultSet.getString("autor_nume"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea cartilor cu autori", e);
        }
        return result;
    }

    public List<String> findCartiPeSectiuni() {
        String sql = "SELECT s.nume AS sectiune, c.titlu " +
                "FROM sectiuni s JOIN sectiune_carti sc ON s.id = sc.sectiune_id " +
                "JOIN carti c ON sc.carte_id = c.id ORDER BY s.nume, c.titlu";
        List<String> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(resultSet.getString("sectiune") + ": " + resultSet.getString("titlu"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea cartilor pe sectiuni", e);
        }
        return result;
    }

    private Carte mapRow(ResultSet resultSet) throws SQLException {
        Autor autor = new Autor(
                resultSet.getString("autor_id"),
                resultSet.getString("autor_nume"),
                resultSet.getString("autor_email"),
                resultSet.getString("nationalitate")
        );
        Carte carte = new Carte(
                resultSet.getString("id"),
                resultSet.getString("titlu"),
                autor,
                resultSet.getString("categorie"),
                resultSet.getBoolean("disponibila")
        );
        carte.setNumarImprumuturi(resultSet.getInt("numar_imprumuturi"));
        return carte;
    }
}
