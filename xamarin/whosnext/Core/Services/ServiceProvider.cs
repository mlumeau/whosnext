using System;
using System.Collections.Generic;
using whosnext.Core.Services.Exceptions;

namespace whosnext.Core.Services
{
	public static class ServiceProvider
	{

		private static Dictionary<String, System.Type> serviceMap = new Dictionary<String, System.Type>{
			{"init", typeof(Firebase.InitService)},
			{"login", typeof(Firebase.LoginService)}
		};


		public static Service Get(String serviceName){
			if (!serviceMap.ContainsKey(serviceName))
			{
				throw new InvalidServiceException(String.Format("The service {0} is not registered in the app", serviceName));
			}

			return ((Service) Activator.CreateInstance (serviceMap [serviceName]));
		}

	}
}

