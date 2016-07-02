using System;
using System.Threading;
using System.Threading.Tasks;

namespace whosnext.Core.Services
{
	public class LoginService : ILoginService
	{
		
		public async Task<bool> isLoggedAsync(){
			await Task.Delay (500).ConfigureAwait(false);
			return true;
		}
		public bool isLogged(){
			return true;
		}


		public bool BasicAuth(String Login, String Password){

			// TODO : Implement login
			Thread.Sleep (500);
			return true;
		}
		public async Task<bool> BasicAuthAsync(String login, String password){

			// TODO : Implement login
			await Task.Delay (500).ConfigureAwait(false);
			return true;
		}

	}
}

