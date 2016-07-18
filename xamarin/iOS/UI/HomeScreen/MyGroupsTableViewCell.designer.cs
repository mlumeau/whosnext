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
    [Register ("MyGroupsTableViewCell")]
    partial class MyGroupsTableViewCell
    {
        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel GroupFrequencyLabel { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel GroupNameLabel { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UILabel GroupNextLabel { get; set; }

        void ReleaseDesignerOutlets ()
        {
            if (GroupFrequencyLabel != null) {
                GroupFrequencyLabel.Dispose ();
                GroupFrequencyLabel = null;
            }

            if (GroupNameLabel != null) {
                GroupNameLabel.Dispose ();
                GroupNameLabel = null;
            }

            if (GroupNextLabel != null) {
                GroupNextLabel.Dispose ();
                GroupNextLabel = null;
            }
        }
    }
}