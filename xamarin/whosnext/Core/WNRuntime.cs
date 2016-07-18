using System;
using System.Threading.Tasks;

using whosnext.Core.Services;
using whosnext.Core.Services.Interfaces;

namespace whosnext.Core
{
	public static class WNRuntime
	{

		public async static Task Init(){
			await ((IInitService)ServiceProvider.Get("init")).InitAsync().ConfigureAwait(false);
		}

	}
}

