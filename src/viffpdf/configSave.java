package viffpdf;
import java.io.Serializable;

public class configSave implements Serializable {
	double dc;
	double dm;
	double dy;
	double dk;
	double bc;
	double bm;
	double by;
	double bk;
	double vc;
	double vm;
	double vy;
	double vk;
	double sc;
	double sm;
	double sy;
	double sk;
	double hc;
	double hm;
	double hy;
	double hk;
	double fc;
	double fm;
	double fy;
	double fk;
	int vfs;
	int mf;
	public configSave() {};
	public configSave(int venueFontSize, double dColorc, double dColorm, double dColory, double dColork,
										 double bColorc, double bColorm, double bColory, double bColork,
										 double vColorc, double vColorm, double vColory, double vColork,
										 double sColorc, double sColorm, double sColory, double sColork,
										 double hColorc, double hColorm, double hColory, double hColork,
										 double fColorc, double fColorm, double fColory, double fColork,
					  int masterFont) {
		vfs = venueFontSize;
		mf = masterFont;
		dc = dColorc;
		dm = dColorm;
		dy = dColory;
		dk = dColork;
		bc = bColorc;
		bm = bColorm;
		by = bColory;
		bk = bColork;
		vc = vColorc;
		vm = vColorm;
		vy = vColory;
		vk = vColork;
		sc = sColorc;
		sm = sColorm;
		sy = sColory;
		sk = sColork;
		hc = hColorc;
		hm = hColorm;
		hy = hColory;
		hk = hColork;
		fc = fColorc;
		fm = fColorm;
		fy = fColory;
		fk = fColork;
	}
	
	void set(char type, char cmyk, double x) {
		switch (type){
		case 'd':
			switch (cmyk) {
			case 'c':
				dc = x;
				return;
			case 'm':
				dm = x;
				return;
			case 'y':
				dy = x;
				return;
			case 'k':
				dk = x;
				return;
			}
		case 'b':
			switch (cmyk) {
			case 'c':
				bc = x;
				return;
			case 'm':
				bm = x;
				return;
			case 'y':
				by = x;
				return;
			case 'k':
				bk = x;
				return;
			}
		case 'v':
			switch (cmyk) {
			case 'c':
				vc = x;
				return;
			case 'm':
				vm = x;
				return;
			case 'y':
				vy = x;
				return;
			case 'k':
				vk = x;
				return;
			}
		case 's':
			switch (cmyk) {
			case 'c':
				sc = x;
				return;
			case 'm':
				sm = x;
				return;
			case 'y':
				sy = x;
				return;
			case 'k':
				sk = x;
				return;
			}
		case 'h':
			switch (cmyk) {
			case 'c':
				hc = x;
				return;
			case 'm':
				hm = x;
				return;
			case 'y':
				hy = x;
				return;
			case 'k':
				hk = x;
				return;
			}
		case 'f':
			switch (cmyk) {
			case 'c':
				fc = x;
				return;
			case 'm':
				fm = x;
				return;
			case 'y':
				fy = x;
				return;
			case 'k':
				fk = x;
				return;
			}
		}
	}
	
	public void setFontSize(int venueFontSize){
		vfs = venueFontSize;
	}
	
	public void setFont(int font) {
		mf = font;
	}
	
	public String getD() {
		String s = null;
		s = Double.toString(dc) + " ";
		s += Double.toString(dm) + " ";
		s += Double.toString(dy) + " ";
		s += Double.toString(dk);
		return s;
	}
	
	public String getB() {
		String s = null;
		s = Double.toString(bc) + " ";
		s += Double.toString(bm) + " ";
		s += Double.toString(by) + " ";
		s += Double.toString(bk);
		return s;
	}
	
	public String getV() {
		String s = null;
		s = Double.toString(vc) + " ";
		s += Double.toString(vm) + " ";
		s += Double.toString(vy) + " ";
		s += Double.toString(vk);
		return s;
	}
	
	public String getS() {
		String s = null;
		s = Double.toString(sc) + " ";
		s += Double.toString(sm) + " ";
		s += Double.toString(sy) + " ";
		s += Double.toString(sk);
		return s;
	}
	
	public String getH() {
		String s = null;
		s = Double.toString(hc) + " ";
		s += Double.toString(hm) + " ";
		s += Double.toString(hy) + " ";
		s += Double.toString(hk);
		return s;
	}
	
	public String getF() {
		String s = null;
		s = Double.toString(fc) + " ";
		s += Double.toString(fm) + " ";
		s += Double.toString(fy) + " ";
		s += Double.toString(fk);
		return s;
	}
}


