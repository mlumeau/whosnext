using System;
using UIKit;
using Foundation;

namespace whosnext.iOS
{
	public class MyGroupsTableViewSource : UITableViewSource
	{

		string[] TableItems = {"Item 1", "Item 2", "Item 3"};

		private UIViewController _controller;

		public MyGroupsTableViewSource(UIViewController ctrl){
			this._controller = ctrl;
		}


		/// DataSource

		public override UITableViewCell GetCell (UITableView tableView, NSIndexPath indexPath){

			MyGroupsTableViewCell cell = tableView.DequeueReusableCell (MyGroupsTableViewCell.Identifier) as MyGroupsTableViewCell;

			if (cell == null){
				tableView.RegisterNibForCellReuse (MyGroupsTableViewCell.Nib, MyGroupsTableViewCell.Identifier);
				cell = tableView.DequeueReusableCell (MyGroupsTableViewCell.Identifier) as MyGroupsTableViewCell;
			}

			string item = TableItems[indexPath.Row];
			cell.UpdateCell (item, "Fridays, 12:00", "rsan, max.lumo");

			return cell;
		}

		public override nint RowsInSection (UITableView tableView, nint section){
			return TableItems.GetLength(0);
		}

		public override nfloat GetHeightForRow (UITableView tableView, NSIndexPath indexPath)
		{
			return 100;
		}


		/// Delegate

		public override void RowSelected (UITableView tableView, NSIndexPath indexPath){
			_controller.PerformSegue ("showGroupDetailSegue", _controller);
			tableView.DeselectRow (indexPath, true);
		}

	}
}

