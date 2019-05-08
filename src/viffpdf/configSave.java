package viffpdf;
import java.io.Serializable;
import java.util.ArrayList;

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
	double oc;
	double om;
	double oy;
	double ok;
	double ec;
	double em;
	double ey;
	double ek;
	int vfs;
	int mf;
	public configSave() {};
	public configSave(int venueFontSize, double dColorc, double dColorm, double dColory, double dColork,
										 double bColorc, double bColorm, double bColory, double bColork,
										 double vColorc, double vColorm, double vColory, double vColork,
										 double sColorc, double sColorm, double sColory, double sColork,
										 double hColorc, double hColorm, double hColory, double hColork,
										 double fColorc, double fColorm, double fColory, double fColork,
										 double oColorc, double oColorm, double oColory, double oColork,
										 double eColorc, double eColorm, double eColory, double eColork,
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
		oc = oColorc;
		om = oColorm;
		oy = oColory;
		ok = oColork;
		ec = eColorc;
		em = eColorm;
		ey = eColork;
		ek = eColork;
	}
	
	void set(char type, ArrayList<Double> list) {
		switch (type){
		case 'd':
			dc = list.get(0);
			dm = list.get(1);
			dy = list.get(2);
			dk = list.get(3);
			break;
		case 'b':
			bc = list.get(0);
			bm = list.get(1);
			by = list.get(2);
			bk = list.get(3);
			break;
		case 'v':
			vc = list.get(0);
			vm = list.get(1);
			vy = list.get(2);
			vk = list.get(3);
			break;
		case 's':
			sc = list.get(0);
			sm = list.get(1);
			sy = list.get(2);
			sk = list.get(3);
			break;
		case 'h':
			hc = list.get(0);
			hm = list.get(1);
			hy = list.get(2);
			hk = list.get(3);
			break;
		case 'f':
			fc = list.get(0);
			fm = list.get(1);
			fy = list.get(2);
			fk = list.get(3);
			break;
		case 'o':
			oc = list.get(0);
			om = list.get(1);
			oy = list.get(2);
			ok = list.get(3);
			break;
		case 'e':
			ec = list.get(0);
			em = list.get(1);
			ey = list.get(2);
			ek = list.get(3);
			break;
		}
	}
	
	public void setFontSize(int venueFontSize){
		vfs = venueFontSize;
	}
	
	public void setFont(int font) {
		mf = font;
	}
	
	public ArrayList<Double> getD() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(dc);
		s.add(dm);
		s.add(dy);
		s.add(dk);
		return s;
	}
	
	public ArrayList<Double> getB() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(bc);
		s.add(bm);
		s.add(by);
		s.add(bk);
		return s;
	}
	
	public ArrayList<Double> getH() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(hc);
		s.add(hm);
		s.add(hy);
		s.add(hk);
		return s;
	}
	
	public ArrayList<Double> getV() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(vc);
		s.add(vm);
		s.add(vy);
		s.add(vk);
		return s;
	}
	
	public ArrayList<Double> getS() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(sc);
		s.add(sm);
		s.add(sy);
		s.add(sk);
		return s;
	}
	
	public ArrayList<Double> getF() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(fc);
		s.add(fm);
		s.add(fy);
		s.add(fk);
		return s;
	}
	
	public ArrayList<Double> getO() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(oc);
		s.add(om);
		s.add(oy);
		s.add(ok);
		return s;
	}
	
	public ArrayList<Double> getE() {
		ArrayList<Double> s = new ArrayList<Double>();
		s.add(ec);
		s.add(em);
		s.add(ey);
		s.add(ek);
		return s;
	}
	

}


