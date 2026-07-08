package com.pao.proiect.biblioteca.repository;

import com.pao.proiect.biblioteca.model.Autor;
import com.pao.proiect.biblioteca.model.Carte;
import com.pao.proiect.biblioteca.model.Cititor;
import com.pao.proiect.biblioteca.model.Imprumut;
import com.pao.proiect.biblioteca.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImprumutRepository implements Repository<Imprumut, String> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public void save(Imprumut entity) {
        String sql = "INSERT INTO imprumuturi (id, carte_id, cititor_id, data_imprumutului, data_returnarii) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, entity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea imprumutului", e);
        }
    }

    @Override
    public Optional<Imprumut> findById(String id) {
        String sql = baseSelect() + " WHERE i.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea imprumutului", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Imprumut> findAll() {
        String sql = baseSelect() + " ORDER BY i.data_imprumutului DESC";
        List<Imprumut> imprumuturi = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                imprumuturi.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea imprumuturilor", e);
        }
        return imprumuturi;
    }

    @Override
    public void update(Imprumut entity) {
        String sql = "UPDATE imprumuturi SET carte_id = ?, cititor_id = ?, data_imprumutului = ?, data_returnarii = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getCarte().getId());
            statement.setString(2, entity.getCititor().getId());
            statement.setDate(3, Date.valueOf(entity.getDataImprumutului()));
            statement.setDate(4, entity.getDataReturnarii() == null ? null : Date.valueOf(entity.getDataReturnarii()));
            statement.setString(5, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea imprumutului", e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM imprumuturi WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea imprumutului", e);
        }
    }

    public void imprumutaCarteTransactional(Imprumut imprumut) {
        String insertSql = "INSERT INTO imprumuturi (id, carte_id, cititor_id, data_imprumutului, data_returnarii) VALUES (?, ?, ?, ?, ?)";
        String updateSql = "UPDATE carti SET disponibila = ?, numar_imprumuturi = numar_imprumuturi + 1 WHERE id = ?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                 PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                fillStatement(insertStatement, imprumut);
                insertStatement.executeUpdate();

                updateStatement.setBoolean(1, false);
                updateStatement.setString(2, imprumut.getCarte().getId());
                updateStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Tranzactia de imprumut a esuat", e);
        }
    }

    public List<String> findImprumuturiActiveCuCititoriSiCarti() {
        String sql = baseSelect() + " WHERE i.data_returnarii IS NULL ORDER BY cititor_nume, c.titlu";
        List<String> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(resultSet.getString("cititor_nume") + " - " + resultSet.getString("titlu"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea imprumuturilor active", e);
        }
        return result;
    }

    public List<String> findNumarImprumuturiPerCititor() {
        String sql = "SELECT ci.nume, COUNT(i.id) AS total " +
                "FROM cititori ci LEFT JOIN imprumuturi i ON ci.id = i.cititor_id " +
                "GROUP BY ci.id, ci.nume ORDER BY total DESC";
        List<String> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(resultSet.getString("nume") + " - " + resultSet.getInt("total") + " imprumuturi");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea imprumuturilor pe cititor", e);
        }
        return result;
    }

    private String baseSelect() {
        return "SELECT i.id, i.data_imprumutului, i.data_returnarii, " +
                "c.id AS carte_id, c.titlu, c.categorie, c.disponibila, c.numar_imprumuturi, " +
                "a.id AS autor_id, a.nume AS autor_nume, a.email AS autor_email, a.nationalitate, " +
                "ci.id AS cititor_id, ci.nume AS cititor_nume, ci.email AS cititor_email, ci.activ " +
                "FROM imprumuturi i " +
                "JOIN carti c ON i.carte_id = c.id " +
                "JOIN autori a ON c.autor_id = a.id " +
                "JOIN cititori ci ON i.cititor_id = ci.id";
    }

    private void fillStatement(PreparedStatement statement, Imprumut entity) throws SQLException {
        statement.setString(1, entity.getId());
        statement.setString(2, entity.getCarte().getId());
        statement.setString(3, entity.getCititor().getId());
        statement.setDate(4, Date.valueOf(entity.getDataImprumutului()));
        statement.setDate(5, entity.getDataReturnarii() == null ? null : Date.valueOf(entity.getDataReturnarii()));
    }

    private Imprumut mapRow(ResultSet resultSet) throws SQLException {
        Autor autor = new Autor(
                resultSet.getString("autor_id"),
                resultSet.getString("autor_nume"),
                resultSet.getString("autor_email"),
                resultSet.getString("nationalitate")
        );
        Carte carte = new Carte(
                resultSet.getString("carte_id"),
                resultSet.getString("titlu"),
                autor,
                resultSet.getString("categorie"),
                resultSet.getBoolean("disponibila")
        );
        carte.setNumarImprumuturi(resultSet.getInt("numar_imprumuturi"));

        Cititor cititor = new Cititor(
                resultSet.getString("cititor_id"),
                resultSet.getString("cititor_nume"),
                resultSet.getString("cititor_email")
        );
        cititor.setActiv(resultSet.getBoolean("activ"));

        Imprumut imprumut = new Imprumut(
                resultSet.getString("id"),
                carte,
                cititor,
                resultSet.getDate("data_imprumutului").toLocalDate()
        );
        Date dataReturnarii = resultSet.getDate("data_returnarii");
        if (dataReturnarii != null) {
            imprumut.setDataReturnarii(dataReturnarii.toLocalDate());
        }
        return imprumut;
    }
}
