using System;
using System.Collections.Generic;

namespace whosnext.Core.Services
{
	public static class ServiceProvider
	{

		private static Dictionary<String, System.Type> _serviceMap = new Dictionary<String, System.Type>{
			{"login", typeof(LoginService)}
		};


		public static Object Get(String serviceName){
			return Activator.CreateInstance (_serviceMap [serviceName]);
		}

	}
}

