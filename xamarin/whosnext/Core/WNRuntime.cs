using System;
using System.Threading.Tasks;

namespace whosnext.Core
{
	public static class WNRuntime
	{

		public async static Task Init(){
			await Task.Delay (500).ConfigureAwait (false);
			Console.WriteLine ("Hi, I'm Initiated");
		}

	}
}

