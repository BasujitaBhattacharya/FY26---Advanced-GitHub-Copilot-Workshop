using System;

namespace LegacyPermitApi
{
    /// <summary>
    /// Permit domain model.
    /// LEGACY: Plain class — should be a record type in .NET 8 for immutability.
    /// MODERN: public record Permit(int Id, string ApplicantName, ...);
    /// </summary>
    public class PermitModel
    {
        public int Id { get; set; }
        public string ApplicantName { get; set; }  // LEGACY: Not nullable-aware — should be string? or required
        public string PermitType { get; set; }
        public string Status { get; set; }
        public string Region { get; set; }
        public DateTime SubmittedDate { get; set; }
        public DateTime? ProcessedDate { get; set; }
        public string Notes { get; set; }
    }
}
