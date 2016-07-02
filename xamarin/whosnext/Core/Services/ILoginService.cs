using System;
using System.Threading.Tasks;

namespace whosnext.Core.Services
{
	public interface ILoginService
	{
		bool isLogged();
		Task<bool> isLoggedAsync();

		bool BasicAuth (String login, String password);
		Task<bool> BasicAuthAsync (String login, String password);
	}
}

