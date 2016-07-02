// WARNING
//
// This file has been generated automatically by Xamarin Studio from the outlets and
// actions declared in your storyboard file.
// Manual changes to this file will not be maintained.
//
using Foundation;
using System;
using System.CodeDom.Compiler;
using UIKit;

namespace whosnext.iOS
{
	[Register ("HomeScreenViewController")]
	partial class HomeScreenViewController
	{
		[Outlet]
		[GeneratedCode ("iOS Designer", "1.0")]
		UIView HomeScreen { get; set; }

		[Outlet]
		[GeneratedCode ("iOS Designer", "1.0")]
		UITableView MyGroupsTable { get; set; }

		void ReleaseDesignerOutlets ()
		{
			if (HomeScreen != null) {
				HomeScreen.Dispose ();
				HomeScreen = null;
			}
			if (MyGroupsTable != null) {
				MyGroupsTable.Dispose ();
				MyGroupsTable = null;
			}
		}
	}
}
