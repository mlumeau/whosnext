using System;

using Foundation;
using UIKit;

namespace whosnext.iOS
{
	public partial class MyGroupsTableViewCell : UITableViewCell
	{
		public static readonly NSString Identifier = new NSString ("MyGroupsTableViewCell");
		public static readonly UINib Nib;


		static MyGroupsTableViewCell ()
		{
			Nib = UINib.FromName ("MyGroupsTableViewCell", NSBundle.MainBundle);
		}

		public MyGroupsTableViewCell(IntPtr handle) : base(handle) {
		}

		public void UpdateCell(string GroupName, string Frequency, string Next){
			GroupNameLabel.Text = GroupName;
			GroupFrequencyLabel.Text = Frequency;
			GroupNextLabel.Text = Next;
		}


	}
}
