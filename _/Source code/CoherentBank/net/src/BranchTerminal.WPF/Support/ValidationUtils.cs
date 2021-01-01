using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace Seovic.Samples.Bank.Support
{
    public static class ValidationUtils
    {
        public static void MarkInvalid(this Control control, string message)
        {
            ControlState state = new ControlState(control.Background, control.ToolTip);

            control.Background = Brushes.PeachPuff;
            control.ToolTip    = message;
            control.GotFocus  += new RoutedEventHandler(state.Reset);
        }
    
        private class ControlState
        {
            public ControlState(Brush background, object tooltip)
            {
                m_background = background;
                m_tooltip    = tooltip;
            }

            public void Reset(object sender, RoutedEventArgs e)
            {
                Control control = (Control) sender;
                control.Background = m_background;
                control.ToolTip    = m_tooltip;
                control.GotFocus -= Reset;
            }

            private readonly Brush  m_background;
            private readonly object m_tooltip;
        }
    }


}