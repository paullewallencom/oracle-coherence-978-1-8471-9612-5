using System.Windows.Threading;
using Tangosol.Net.Cache;
using Tangosol.Net.Cache.Support;

namespace Seovic.Samples.Bank.Support
{
    /// <summary>
    /// Ensures that any event handling code that needs to run
    /// as a response to a cache event is executed on the WPF UI thread.
    /// </summary>
    /// <remarks>
    /// This class allows end users to handle Coherence cache events which are
    /// always raised from a background thread, as if they were raised within
    /// the WPF UI thread. This class will ensure that the call is properly 
    /// marshalled and executed on the WPF UI thread.
    /// </remarks>
    /// <author>Ivan Cikic  2009.12.10</author>
    public class WpfCacheListener : DelegatingCacheListener, ICacheListener
    {
        private delegate void CacheEventCallback(CacheEventArgs args);

        private readonly DispatcherObject control;

        /// <summary>
        /// Creates an instance of this class.
        /// </summary>
        /// <param name="control">
        /// Control that should be used to determine UI thread
        /// and marshal events appropriately.
        /// </param>
        public WpfCacheListener(DispatcherObject control)
        {
            this.control = control;
        }

        /// <summary>
        /// Invoked when a dictionary entry has been inserted.
        /// </summary>
        /// <param name="evt">
        /// The <see cref="CacheEventArgs"/> carrying the insert
        /// information.
        /// </param>
        void ICacheListener.EntryInserted(CacheEventArgs evt)
        {
            CacheEventCallback callback = new CacheEventCallback(OnInserted);
            control.Dispatcher.Invoke(DispatcherPriority.Normal, callback, evt);
        }

        /// <summary>
        /// Invoked when a dictionary entry has been updated.
        /// </summary>
        /// <param name="evt">
        /// The <see cref="CacheEventArgs"/> carrying the update
        /// information.
        /// </param>
        void ICacheListener.EntryUpdated(CacheEventArgs evt)
        {
            CacheEventCallback callback = new CacheEventCallback(OnUpdated);
            control.Dispatcher.Invoke(DispatcherPriority.Normal, callback, evt);
        }

        /// <summary>
        /// Invoked when a dictionary entry has been deleted.
        /// </summary>
        /// <param name="evt">
        /// The <see cref="CacheEventArgs"/> carrying the remove
        /// information.
        /// </param>
        void ICacheListener.EntryDeleted(CacheEventArgs evt)
        {
            CacheEventCallback callback = new CacheEventCallback(OnDeleted);
            control.Dispatcher.Invoke(DispatcherPriority.Normal, callback, evt);
        }
    }
}