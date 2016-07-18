using Foundation;
using System;
using System.CodeDom.Compiler;
using System.Threading.Tasks;
using UIKit;

using whosnext.Core;
using whosnext.Core.Services;
using whosnext.Core.Services.Interfaces;

namespace whosnext.iOS
{
	partial class SplashScreenViewController : UIViewController
	{
		public SplashScreenViewController (IntPtr handle) : base (handle)
		{
		}


		public async override void ViewDidAppear (bool animated)
		{
			base.ViewDidAppear (animated);


			await WNRuntime.Init ();


			bool logged = await ( (ILoginService) ServiceProvider.Get ("login")).isLoggedAsync ();

			if (logged) {
				this.PerformSegue ("splashToHomeSegue", this);
			} else {
				this.PerformSegue ("splashToLoginSegue", this);
			}
		}
	}
}
