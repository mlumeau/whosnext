using System;
namespace whosnext.Core.Services.Exceptions
{
	public class InvalidServiceException : Exception
	{
		public InvalidServiceException()
		{
		}

		public InvalidServiceException(string message)
        : base(message)
    	{
		}

		public InvalidServiceException(string message, Exception inner)
        : base(message, inner)
    	{
		}
	}
}

