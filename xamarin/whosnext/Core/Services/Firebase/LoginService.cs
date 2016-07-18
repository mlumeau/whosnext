using System;
using System.Threading.Tasks;
using FireSharp.Response;
using Foundation;
using whosnext.Core.Services;
using whosnext.Core.Services.Interfaces;
using whosnext.Utils;

namespace whosnext.Core.Services.Firebase
{
	public class LoginService : Service, ILoginService
	{

		public const string LOGIN_PREF_KEY = "wn.firebase.login";

		public bool BasicAuth(string login, string password)
		{
			FirebaseResponse res = ClientManager.GetManager().getClient().Get($"users/{login}");

			if( res.ResultAs<Model.User>() == null ){
				return false;
			}
			return true;
		}

		public async Task<bool> BasicAuthAsync(string login, string password)
		{
			FirebaseResponse res = await ClientManager.GetManager().getClient().GetAsync($"users/{login}");

			if (res.ResultAs<Model.User>() == null)
			{
				return false;
			}

#if __IOS__
			NSUserDefaults stdStore = NSUserDefaults.StandardUserDefaults;
			stdStore.SetString(login, LOGIN_PREF_KEY);
			if (!stdStore.Synchronize())
			{
				Log.error("Login", "Session could not be saved");
				return false;
			}
#elif __ANDROID__
			// TODO : Implement Session storage for Android
#endif

			return true;
		}

		public bool isLogged()
		{
#if __IOS__
			NSUserDefaults stdStore = NSUserDefaults.StandardUserDefaults;
			if (stdStore.StringForKey(LOGIN_PREF_KEY) != null)
			{
				return true;
			}
#elif __ANDROID__
			// TODO : Implement Session storage for Android
#endif
			return false;
		}

		public async Task<bool> isLoggedAsync()
		{
			return this.isLogged();
		}
	}
}

