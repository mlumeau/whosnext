using Foundation;
using System;
using System.CodeDom.Compiler;
using UIKit;

namespace whosnext.iOS
{
	partial class HomeScreenViewController : UIViewController
	{
		public HomeScreenViewController (IntPtr handle) : base (handle)
		{
		}

		public override void ViewDidLoad(){
			MyGroupsTable.Source = new MyGroupsTableViewSource(this);
		}


		public override void PrepareForSegue (UIStoryboardSegue segue, NSObject sender)
		{
			base.PrepareForSegue(segue, sender);

			switch (segue.Identifier){
			case "showGroupDetailSegue":
				((GroupDetailScreenViewController)segue.DestinationViewController).Index = MyGroupsTable.IndexPathForSelectedRow.Row;
				break;
			}
		}

	}
}
