using System;
namespace whosnext.Utils
{
	public static class Log
	{
		public static void debug(string group, string mess)
		{
			Console.WriteLine($"[DEBUG] - [{group}] {mess}");
		}

		public static void info(string group, string mess)
		{
			Console.WriteLine($"[INFO]  - [{group}] {mess}");
		}

		public static void warning(string group, string mess)
		{
			Console.WriteLine($"[WARN]  - [{group}] {mess}");
		}

		public static void error(string group, string mess)
		{
			Console.WriteLine($"[ERR]   - [{group}] {mess}");
		}

		public static void fatal(string group, string mess)
		{
			Console.WriteLine($"[FATAL] - [{group}] {mess}");
		}
	}
}

