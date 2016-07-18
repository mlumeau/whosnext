using System;
using FireSharp;
using FireSharp.Config;
using FireSharp.Interfaces;
using whosnext.Core.Services.Exceptions;

namespace whosnext.Core.Services.Firebase
{
	public class ClientManager
	{

		private static ClientManager instance;

		private IFirebaseConfig config;
		private IFirebaseClient client;

		private ClientManager()
		{
			config = new FirebaseConfig
			{
				AuthSecret = "{{ PRIVATE_KEY }}",
				BasePath = "https://whos-next-71703.firebaseio.com"
			};
			client = new FirebaseClient(config);
		}

		public static ClientManager GetManager()
		{
			if (instance == null)
			{
				instance = new ClientManager();
			}

			return instance;
		}

		public IFirebaseClient getClient()
		{
			if (client == null)
			{
				throw new InvalidServiceException("The Firebase Client has not been initialized. Please call the 'init' service before");
			}

			return client;
		}


	}
}

