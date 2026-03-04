// App_Start/WebApiConfig.cs
// LEGACY: Web API 2 startup class — registered in Global.asax via WebApiConfig.Register(GlobalConfiguration.Configuration)
// MODERN: Use Program.cs + builder.Services.AddControllers() in .NET 8 minimal API

using System.Web.Http;

namespace OntarioPermits.App_Start
{
    /// <summary>Web API 2 routing and configuration entry point.</summary>
    /// <remarks>
    /// LEGACY: This pattern is replaced by Program.cs in ASP.NET Core.
    /// All configuration is done via the HttpConfiguration object rather than
    /// the modern WebApplicationBuilder / IServiceCollection pattern.
    /// </remarks>
    public static class WebApiConfig
    {
        /// <summary>Registers Web API routes and configures formatters.</summary>
        /// <param name="config">The global HTTP configuration object.</param>
        public static void Register(HttpConfiguration config)
        {
            // LEGACY: Attribute routing must be explicitly enabled.
            // MODERN: Enabled by default via builder.Services.AddControllers()
            config.MapHttpAttributeRoutes();

            // LEGACY: Convention-based route template.
            // MODERN: app.MapControllers() with [Route] attributes on controllers.
            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );

            // LEGACY: Manually remove XML formatter to force JSON responses.
            // MODERN: Configure via builder.Services.AddControllers().AddJsonOptions(...)
            var xmlFormatter = config.Formatters.XmlFormatter;
            config.Formatters.Remove(xmlFormatter);

            // LEGACY: Enabling CORS globally via a filter.
            // MODERN: builder.Services.AddCors() + app.UseCors() in Program.cs
            // config.EnableCors(); // Requires Microsoft.AspNet.WebApi.Cors NuGet package
        }
    }
}
