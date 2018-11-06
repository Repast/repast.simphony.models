package geography.styles;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.AbstractStyleVisitor;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.builder.ColorMapBuilder;

import repast.simphony.visualization.gis3D.style.CoverageStyle;

/**
 * Coverage style that colors the elements from white to blue based on value.
 * 
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class BlueCoverageStyle<T> implements CoverageStyle<T> {

	public class SymbolizerCollector extends AbstractStyleVisitor {
		List<Symbolizer> symbs = new ArrayList<Symbolizer>();
		
		@Override
		public void visit(Rule rule) {
			for (Symbolizer sym : rule.getSymbolizers()) {
				sym.accept(this);
				symbs.add(sym);
			}
		}
		
		public List<Symbolizer> getSymbolizers(){
			return symbs;
		}
	}
	
	@Override
	public RasterSymbolizer getSymbolizer() {		
		 // Blue colormap
		 ColorMapBuilder cm = new ColorMapBuilder();
		 		 
		 cm.entry().quantity(0).colorHex("#ffffff");
		 cm.entry().quantity(1).colorHex("#f7fbff");
		 cm.entry().quantity(2).colorHex("#deebf7");
		 cm.entry().quantity(3).colorHex("#c6dbef");
		 cm.entry().quantity(4).colorHex("#9ecae1");
		 cm.entry().quantity(5).colorHex("#6baed6");
		 cm.entry().quantity(6).colorHex("#4292c6");
		 cm.entry().quantity(7).colorHex("#2171b5");
		 cm.entry().quantity(8).colorHex("#08519c");
		 cm.entry().quantity(9).colorHex("#08306b");
		 
     Style style = cm.buildStyle();

     SymbolizerCollector collector = new SymbolizerCollector();
     style.accept(collector);
     
     RasterSymbolizer sym = (RasterSymbolizer)collector.getSymbolizers().get(0);

     return sym;
	}

	@Override
	public boolean isSmoothed() {
		return false;
	}

	@Override
	public double getOpacity() {
		return 1.0;
	}
}