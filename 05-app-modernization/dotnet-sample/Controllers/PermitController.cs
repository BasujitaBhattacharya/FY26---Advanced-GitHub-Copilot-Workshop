using System;
using System.Configuration;
using System.Web.Http;

namespace LegacyPermitApi.Controllers
{
    /// <summary>
    /// Legacy ASP.NET Web API 2 controller for permits.
    /// LEGACY: Uses ApiController base class (Web API 2, .NET Framework only)
    /// LEGACY: Uses ConfigurationManager for settings
    /// LEGACY: No async/await — all synchronous operations
    /// TARGET: Migrate to ASP.NET Core minimal API or controller on .NET 8
    /// </summary>
    public class PermitController : ApiController
    {
        // LEGACY: Direct instantiation — should use constructor injection with IPermitRepository
        private readonly PermitRepository _repository;

        public PermitController()
        {
            // LEGACY: Reading connection string from Web.config via ConfigurationManager
            // MODERN: Inject IConfiguration and use _config.GetConnectionString("DefaultConnection")
            string connectionString = ConfigurationManager.ConnectionStrings["DefaultConnection"].ConnectionString;
            _repository = new PermitRepository(connectionString);
        }

        // GET api/permits?page=1&pageSize=25
        // LEGACY: Synchronous action — blocks the thread
        // MODERN: public async Task<IHttpActionResult> GetAll() with await
        [HttpGet]
        [Route("api/permits")]
        public IHttpActionResult GetAll([FromUri] int page = 1, [FromUri] int pageSize = 25)
        {
            if (page < 1)
                return BadRequest("page must be greater than 0");

            if (pageSize < 1)
                return BadRequest("pageSize must be greater than 0");

            try
            {
                var permits = _repository.GetAll(page, pageSize); // LEGACY: sync call
                return Ok(permits);
            }
            catch (Exception ex)
            {
                // LEGACY: Logging to Console — should use ILogger<PermitController>
                Console.WriteLine("Error in GetAll: " + ex.Message);
                return InternalServerError(ex);
            }
        }

        // GET api/permits/5
        [HttpGet]
        [Route("api/permits/{id:int}")]
        public IHttpActionResult GetById(int id)
        {
            try
            {
                var permit = _repository.GetById(id); // LEGACY: sync call
                if (permit == null)
                    return NotFound();
                return Ok(permit);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error in GetById: " + ex.Message);
                return InternalServerError(ex);
            }
        }

        // POST api/permits
        [HttpPost]
        [Route("api/permits")]
        public IHttpActionResult Create([FromBody] PermitModel model)
        {
            if (model == null)
                return BadRequest("Permit data is required");

            // LEGACY: No FluentValidation — manual validation scattered in controller
            if (string.IsNullOrEmpty(model.ApplicantName))
                return BadRequest("ApplicantName is required");

            try
            {
                int newId = _repository.Create(model); // LEGACY: sync call
                return Created($"api/permits/{newId}", new { Id = newId });
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error in Create: " + ex.Message);
                return InternalServerError(ex);
            }
        }
    }
}
