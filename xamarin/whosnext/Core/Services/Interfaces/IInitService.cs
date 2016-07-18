using System;
using System.Threading.Tasks;

namespace whosnext.Core.Services.Interfaces
{
	public interface IInitService
	{
		bool Init();
		Task<bool> InitAsync();
	}
}

