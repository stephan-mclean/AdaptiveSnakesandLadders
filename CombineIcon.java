/*
	Class which we will use to layout player pieces
	side by side on a button.
*/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
class CombineIcon implements Icon {
		private ArrayList<ImageIcon> icons;

		public CombineIcon() {
			icons = new ArrayList<ImageIcon>();
		}

		public int getIconHeight() {
			if(icons.size() > 0) {
				return icons.get(0).getIconHeight();
			}
			else {
				return 0;
			}
		}

		public int getIconWidth() {
			int width = 0;

			for(Icon i : icons) {
				width += i.getIconWidth();
			}
			return width;
		}

		void add(ImageIcon i) {
			icons.add(i);
		}

		void remove(ImageIcon icon) {
			for(int i = 0; i < icons.size(); i++) {
				if(icons.get(i).getDescription().equals(icon.getDescription())) {
					icons.remove(i);
					break;
				}
			}
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {

			int newX = x;
			for(int i = 0; i < icons.size(); i++) {
				icons.get(i).paintIcon(c, g, newX, y);
				newX += icons.get(i).getIconWidth();
			}
		}
}