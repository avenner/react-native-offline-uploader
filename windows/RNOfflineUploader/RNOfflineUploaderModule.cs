using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Offline.Uploader.RNOfflineUploader
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNOfflineUploaderModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNOfflineUploaderModule"/>.
        /// </summary>
        internal RNOfflineUploaderModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNOfflineUploader";
            }
        }
    }
}
