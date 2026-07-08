package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Cititor;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CititorRepository implements Repository<Cititor, String> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Cititor entity) {
        String sql = "INSERT INTO cititori (id, nume, email, activ) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getId());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getEmail());
            statement.setBoolean(4, entity.isActiv());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cititorului", e);
        }
    }

    @Override
    public Optional<Cititor> findById(String id) {
        String sql = "SELECT id, nume, email, activ FROM cititori WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cititorului", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cititor> findAll() {
        String sql = "SELECT id, nume, email, activ FROM cititori";
        List<Cititor> cititori = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                cititori.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cititorilor", e);
        }
        return cititori;
    }

    @Override
    public void update(Cititor entity) {
        String sql = "UPDATE cititori SET nume = ?, email = ?, activ = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getEmail());
            statement.setBoolean(3, entity.isActiv());
            statement.setString(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cititorului", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM cititori WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cititorului", e);
        }
    }

    private Cititor mapRow(ResultSet resultSet) throws SQLException {
        Cititor cititor = new Cititor(
                resultSet.getString("id"),
                resultSet.getString("nume"),
                resultSet.getString("email")
        );
        cititor.setActiv(resultSet.getBoolean("activ"));
        return cititor;
    }
}
