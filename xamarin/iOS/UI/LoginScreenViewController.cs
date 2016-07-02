using Foundation;
using System;
using System.CodeDom.Compiler;
using System.Threading.Tasks;
using UIKit;

using whosnext.Core.Services;

namespace whosnext.iOS
{
	partial class LoginScreenViewController : UIViewController
	{
		public LoginScreenViewController (IntPtr handle) : base (handle)
		{
			
		}

		public override void ViewDidAppear(bool animated){

			base.ViewDidAppear (animated);

			// Register Login Button Listener
			LoginButton.TouchUpInside += LoginAction;

		}



		public async void LoginAction(object sender, EventArgs e){
			if (LoginTextField.Text.Length <= 0 || PasswordTextField.Text.Length <= 0){
				Console.WriteLine("Login and Password fields are required");
				return;
			}

			bool isLogged = await ((ILoginService) ServiceProvider.Get("login")).BasicAuthAsync(LoginTextField.Text, PasswordTextField.Text);

			if (isLogged) {
				this.PerformSegue ("loginToHomeSegue", this);
			}
		}

	}
}
