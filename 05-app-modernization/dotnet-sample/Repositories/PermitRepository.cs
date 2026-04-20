using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;  // LEGACY: System.Data.SqlClient — should use Microsoft.Data.SqlClient in .NET 8

namespace LegacyPermitApi
{
    /// <summary>
    /// Data access layer for permits.
    /// LEGACY: Uses System.Data.SqlClient (not supported in .NET 8 — requires Microsoft.Data.SqlClient)
    /// LEGACY: All methods are synchronous — should use async/await with SqlCommand.ExecuteReaderAsync
    /// LEGACY: Raw ADO.NET — consider Entity Framework Core in .NET 8
    /// LEGACY: Connection string passed as constructor arg — should inject IConfiguration
    /// </summary>
    public class PermitRepository
    {
        // LEGACY: Storing connection string in field — fine, but IConfiguration is the .NET 8 way
        private readonly string _connectionString;

        public PermitRepository(string connectionString)
        {
            // LEGACY: No null check — .NET 8: ArgumentNullException.ThrowIfNull(connectionString)
            _connectionString = connectionString;
        }

        // LEGACY: Synchronous — blocks thread for duration of DB call
        // MODERN: public async Task<IReadOnlyList<PermitModel>> GetAllAsync()
        public List<PermitModel> GetAll()
        {
            return GetAll(1, 25);
        }

        public List<PermitModel> GetAll(int page, int pageSize)
        {
            var permits = new List<PermitModel>();
            var offset = (page - 1) * pageSize;

            // LEGACY: Using statement with manual SqlConnection/SqlCommand construction
            using (var connection = new SqlConnection(_connectionString))
            {
                connection.Open(); // LEGACY: sync Open — use await connection.OpenAsync()
                var command = new SqlCommand(
                    @"SELECT * FROM Permits
                      ORDER BY PermitId
                      OFFSET @offset ROWS
                      FETCH NEXT @pageSize ROWS ONLY",
                    connection);
                command.Parameters.AddWithValue("@offset", offset);
                command.Parameters.AddWithValue("@pageSize", pageSize);
                var reader = command.ExecuteReader(); // LEGACY: sync — use await ExecuteReaderAsync()

                while (reader.Read()) // LEGACY: sync Read — use await reader.ReadAsync()
                {
                    permits.Add(MapPermit(reader));
                }
            }

            return permits;
        }

        // LEGACY: Synchronous — should be async
        public PermitModel GetById(int id)
        {
            using (var connection = new SqlConnection(_connectionString))
            {
                connection.Open();
                var command = new SqlCommand("SELECT * FROM Permits WHERE PermitId = @id", connection);
                command.Parameters.AddWithValue("@id", id);
                var reader = command.ExecuteReader();

                if (reader.Read())
                    return MapPermit(reader);

                return null; // LEGACY: returning null — .NET 8 nullable: PermitModel?
            }
        }

        // LEGACY: Synchronous create — should be async
        public int Create(PermitModel model)
        {
            using (var connection = new SqlConnection(_connectionString))
            {
                connection.Open();
                var command = new SqlCommand(
                    @"INSERT INTO Permits (ApplicantName, PermitType, Status, Region, SubmittedDate, Notes)
                      OUTPUT INSERTED.PermitId
                      VALUES (@name, @type, @status, @region, @date, @notes)",
                    connection);

                command.Parameters.AddWithValue("@name", model.ApplicantName);
                command.Parameters.AddWithValue("@type", model.PermitType);
                command.Parameters.AddWithValue("@status", "Pending");
                command.Parameters.AddWithValue("@region", model.Region);
                command.Parameters.AddWithValue("@date", DateTime.UtcNow);
                command.Parameters.AddWithValue("@notes", model.Notes ?? (object)DBNull.Value);

                return (int)command.ExecuteScalar(); // LEGACY: sync — use await ExecuteScalarAsync()
            }
        }

        private PermitModel MapPermit(SqlDataReader reader)
        {
            return new PermitModel
            {
                Id = (int)reader["PermitId"],
                ApplicantName = reader["ApplicantName"].ToString(),
                PermitType = reader["PermitType"].ToString(),
                Status = reader["Status"].ToString(),
                Region = reader["Region"].ToString(),
                SubmittedDate = (DateTime)reader["SubmittedDate"],
                ProcessedDate = reader["ProcessedDate"] == DBNull.Value ? (DateTime?)null : (DateTime)reader["ProcessedDate"],
                Notes = reader["Notes"] == DBNull.Value ? null : reader["Notes"].ToString()
            };
        }
    }
}
