using System;
using System.Threading.Tasks;
using whosnext.Core.Services.Interfaces;

namespace whosnext.Core.Services.Mockup
{
	public class InitService : Service, IInitService
	{
		public bool Init()
		{
			return true;
		}

		public async Task<bool> InitAsync()
		{
			await Task.Delay(0).ConfigureAwait(false);
			return true;
		}
	}
}

