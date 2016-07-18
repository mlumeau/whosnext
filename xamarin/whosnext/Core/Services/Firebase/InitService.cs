using System;
using System.Threading.Tasks;
using whosnext.Core.Services;
using whosnext.Core.Services.Interfaces;
namespace whosnext.Core.Services.Firebase
{
	public class InitService : Service, IInitService
	{

		public bool Init()
		{
			// Init the Firebase client
			ClientManager.GetManager().getClient();
			return true;
		}

		#pragma warning disable CS1998 // Async method lacks 'await' operators and will run synchronously
		public async Task<bool> InitAsync()
		#pragma warning restore CS1998 // Async method lacks 'await' operators and will run synchronously
		{
			this.Init();
			return true;
		}
	}
}

