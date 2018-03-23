package logica_difusa;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import net.sourceforge.jFuzzyLogic.FIS;

@SuppressWarnings("serial")
public class ventana extends JFrame implements ActionListener
{
	JFrame frame;
	JButton inicia, reinicia;
	JLabel et1, et2, et3, et4; 
	JPanel pn1, pn2, pn3;
	//etiquetas para pn2
	JLabel velocidadn, alturan;
	public double vel=0, alt=0;
	
	panelImagen pi;
	JTextField c1, c2;

	//muestra el exito de la simulacion
	resultado ico = new resultado();
	
	//variables necesarias para el funcionamiento
	public int altm = 100, con=0;
	public int altActual = 460; 
	public double acele=0;
	public double altu=0;
	
	public ventana()
	{
		//metodo para crear la interfaz
		interfaz();
	}
	
	public void interfaz()
	{
		//frame
		frame = new JFrame();
		frame.setTitle("Nave - Sistema difuso - Caida Libre");
		frame.setSize(600,500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		
		//agragamos paneles
		pi = new panelImagen();
		pi.setBounds(250, 0, 350, 500);
		frame.add(pi);
		
		//etiqueta programa
		et3 = new JLabel("Sistema Difuso - Caida Libre");
		et3.setBounds(20, 20, 200, 20);
		et3.setFont(new Font("Arial", Font.BOLD, 15));
		frame.add(et3);
		
		//panel de datos
		pn1 = new JPanel();
		pn1.setLayout(new GridLayout(4,1));
		pn1.setBounds(50, 50, 150, 100);
		pn1.add(et1 = new JLabel("Altura - Km - max=100km"));
		et1.setHorizontalAlignment(SwingConstants.CENTER);
		pn1.add(c1 = new JTextField());
		pn1.add(et2 = new JLabel("Peso - Kg"));
		et2.setHorizontalAlignment(SwingConstants.CENTER);
		pn1.add(c2 = new JTextField());
		frame.add(pn1);

		//panel de resultados
		pn2 = new JPanel();
		pn2.setLayout(new GridLayout(1,2));
		pn2.setBounds(10, 450, 250, 20);
		pn2.add(velocidadn = new JLabel("Velocidad: "+vel+" m/s"));
		velocidadn.setHorizontalAlignment(SwingConstants.CENTER);
		pn2.add(alturan = new JLabel("Altura: "+alt+" Mts"));
		alturan.setHorizontalAlignment(SwingConstants.CENTER);
		frame.add(pn2);
		
		//panel de botones
		pn3 = new JPanel();
		pn3.setLayout(new GridLayout(1,2));
		pn3.setBounds(40, 180, 170, 20);
		pn3.add(reinicia = new JButton("Reiniciar"));
		pn3.add(inicia = new JButton("Iniciar"));
		frame.add(pn3);
		
		//agregar icono
		ico.setBounds(100, 250, 64, 64);
		frame.add(ico);
		ico.setVisible(false);
		
		inicia.addActionListener(this);
		reinicia.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent a)
	{
		//boton para reiniciar los campos
		if (a.getSource() == reinicia)
		{
			c1.setText("");
			c2.setText("");
			velocidadn.setText("Velocidad: 0 m/s");
			alturan.setText("Altura: 100 km");
			ico.setVisible(false);
			frame.repaint();
			
			pi.y=0;
			pi.repaint();
		}
		//boton para iniciar el programa
		if(a.getSource() == inicia)
		{
			//obtenemos la altura ingresada
			int prualt = Integer.parseInt(c1.getText());
			//comparamos la altura ingresada con la altura maxima
			if (prualt<=altm)
			{
				//worker para utilizar hilos
				final SwingWorker<?, ?> hilo = new SwingWorker<Object, Object>()
				{
					protected Void doInBackground() throws Exception
					{
						//direccion del archivo FCL
						String fileName = "C:\\Users\\Ricardo\\Documents\\eclipse\\IA\\src\\logica_difusa\\tabla.fcl";
						
						//cargamos archivo FCL
						FIS fis = FIS.load(fileName, true);
						
						//comprobamos la existencia del archivo
					    if( fis == null )
					    {
					    	System.err.println("No se pudo cargar archivo: "+ fileName + "");
					    	System.exit(0);
					    }
						
					    //variables a utilizar
					    int altura = Integer.parseInt(c1.getText());
					    int peso = Integer.parseInt(c2.getText());
					    double vel = 0, dist1=0;
					    
					    //vector de porcentajes
					    double porcen [] = {.05,.10,.15,.20,.25,.30,.35,.40,.45,.50};
					    
					    //imprimimos las variables
					    System.out.println("Altura: "+altura+"Km  Peso: "+peso+" Kg");
					    
					    //establecemos la ubicacion inicial
					    double altini = 0 , distf = 0, alt = 0;
					    
					    //redondear numeros
					    DecimalFormat df = new DecimalFormat("#.00");
					    
					    //configurar altura ingresada en el panel
					    if (altini == 0)
					    {
					    	//variable de altura
					    	alt = posicion(altura);
					    	
					    	//convertimos altura px a km
					    	distf = alt / 4.6;
					    	
					    	//distancia del suelo
					    	altu = distf;
					    	
					    	//enviamos distancia del suelo
					    	alturan.setText("Altura: "+df.format(altu)+" Km");

					    	//ingresamos nueva posicion de la nave
					    	pi.y = (int) alt;
					    	
					    	//repintamos el panel
							pi.repaint();
							
							//esperamos 1 segundo a que empieze
							Thread.sleep(1000);
							
							//cambiamos variable de altura inicial
							altini=1;
						}
					    
					    //variables de validacion
					    int v1=0;
					    
					    //iniciamos calculo
						while(true)
						{
							//aceleracion
							if (v1 == 0)
							{
								//velocidad inicial = 0
								vel = velocidad(0);
								//enviamos velocidad
								velocidadn.setText("Velocidad: "+df.format(vel)+" m/s2");
								v1=1;
							}else {
								//calculamos nueva velocidad
								vel = velocidad(vel);
								//enviamos velocidad
								velocidadn.setText("Velociad: "+df.format(vel)+" m/s2");
							}
							
							//velocidad
							System.out.println("Velocidad actual: "+df.format(vel)+" m/s2");
							
							//variable acumulativa de aceleracion
							acele = acele + vel;
							
							//validacion necesaria para realizar movimiento de la nave
							//cada 1000mts de velocidad se reducira 1km
							if (acele>1000)
							{
								//obtenemos kilometros a reducir
								int i = (int) acele / 1000;
								
								//convertimos los km a pixeles
								int p = i * (int) 4.6;
								
								//agregamos la nueva posicion en px
								alt = alt + p;
								
								//calculamos la distancia del suelo
								distf = (altActual-alt) / 4.6;
								
								//enviamos posicion de la nave
								alturan.setText(df.format(distf)+" Km");
						    	
								//actualizamos posicion
								pi.y = (int) alt;
								pi.repaint();
								Thread.sleep(100);
								
								//validamos que la distancia en px no supere
								//el limite permitido
								if (alt>460) {
									alturan.setText("0 Km");
									pi.y = (int) alt;
									pi.repaint();
									Thread.sleep(100);
								}
								
								//convertimos a km
								i = i * 1000;
								
								//actualizamos variable acumulativa
								acele = acele - i;
							}

							if(distf>=0 && distf<=20)
			                {
								//variables
			                	double velDif = vel;
			                	double altDif=0; 
			                	int a = (int) dist1;

			                	//obtenemos nivel de altura
			                	altDif = fuzzy(a);
			                	
			                	//enviamos variables a tablas
			                	fis.setVariable("velocidad", velDif);
			                	fis.setVariable("altura", altDif);
			                	
			                	//evaluamos las tablas
			                	fis.evaluate();
			                	
			                	//obtenemos propulsion
			                	double p = fis.getVariable("propulsion").getValue();
			                	
			                	//redondeamos valor de propulsion a nivel mas proximo
			                	p = Math.round(p);
			                	
			                	//imprimimos valor de propulsion
			                	System.out.println("Fuerza e propulsion: "+p+"\n");
			                	
			                	//variables
			                	int valp = (int) p;
			                	double nv=0, np;
			                	
			                	//aplicar niveles de propulsion
			                	for(int i=0; i<10; i++)
			                	{
			                		if(valp==(i+1))
			                		{
			                			//velocidad a restar
			                			np = vel * porcen[i];
			                			//nueva velocidad
			                			nv = vel - np;
			                			vel = nv;
			                			break;
			                		}
			                	}
			                	
			                }
							
							//variables
							double pe, at;
							
							//validamos que la nave alla terminado de descender
							if(alt>460) {
								
								pe = peso*1.0;
								
				                //comprobar estado de la mision
				                at = impacto(vel, pe);

				                //enviamos nueva posicion
								pi.y = 428;
								pi.repaint();
								Thread.sleep(100);

								//validamos el exito o fracaso de la mision
								if(at==1)
			                    {
									//muestra imagen donde constatamos el fallido aterrizaje
			                    	ico.setToolTipText("La nave quedo destruida");
			                    	ico.setIcon(new ImageIcon(getClass().getResource("error.png")));
			                    	ico.setVisible(true);
			                    	frame.repaint();
			                        break;
			                    }else{
			                    	//muestra imagen donde validamos el exito del aterrizaje
			                    	ico.setToolTipText("La nave aterrizo con exito");
			                    	ico.setIcon(new ImageIcon(getClass().getResource("comprobado.png")));
			                    	ico.setVisible(true);
			                    	frame.repaint();
			                    	break;
			                    }
			                }
						}
						return null;
					}
					//impresion de fin del programa
					protected void done() 
		              {
		                JOptionPane.showMessageDialog(null, "Fin de aterrizaje", "Info", JOptionPane.INFORMATION_MESSAGE);
		              }
				};
				hilo.execute();
			}else {
				//en caso de superar la altura maxima
				JOptionPane.showMessageDialog(null, "La altura sobrepasa el limite permitido", 
						"Info: Altura no permitida", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	public double posicion(double ubicacion)
	{
		//metodo para calcular la posicion inicial
		//variables
		double px = 4.6;
		int altfinal = 0;
		
		//si se encuentra en la altura maxima
		if (ubicacion == 100) {
			if (con == 0) {
				altfinal = 0;
				con=1;
			}
		}else {
			//convertimos altura en pixeles
			altfinal = (int) (ubicacion * px);
			//calculamos la diferencia de altura
			altfinal = altActual - altfinal;
		}
		return altfinal;
	}
	public double velocidad(double velocidad)
	{
		//calcular velocidad de la nave
		//variables
		double gravedad = 9.81, aceleracion = 0;
		
		//en caso de que sea la primera vez
		if (velocidad == 0)
		{
			//velocidad inicial 0
			//asignamos gravedad
			aceleracion = gravedad;
		}else {
			//nueva velocidad
			aceleracion = velocidad + gravedad;
		}
		return aceleracion;
	}
	public double impacto(double aceleracion, double peso)
	{
		//metodo que nos garantiza el exito o fracaso de la mision
		//variables
		double mf, c=0, v1, v2, v3;
		
        //formula de fuerza de impacto
        //	F = (1/2) * m * a^2
		//aplicamos formula
		v1 = aceleracion * aceleracion;
		v2 = peso * v1;
		v3 = v2 * 0.5;
		
		//realizamos conversion a kg
		v3 = v3 * 0.102;
		
		//resistencia de la nave
		mf = peso * 2000;
		
		//fuerza de impacto
        if(v3>mf)
        {
            c=1.0;
        }
		
		return c;
	}
    public static int fuzzy(int km)
    {
    	//metodo para calcular valor de altura
    	int a=0;

    	if(km>=0 && km<=1)
    	{
    		a = 1;
    	}
    	if(km>=1 && km<=2)
    	{
    		a = 2;
    	}
    	if(km>=2 && km<=3)
    	{
    		a = 3;
    	}
    	if(km>=3 && km<=6)
    	{
    		a = 4;
    	}
    	if(km>=6 && km<=9)
    	{
    		a = 5;
    	}
    	if(km>=9 && km<=11)
    	{
    		a = 6;
    	}
    	if(km>=11 && km<=14)
    	{
    		a = 7;
    	}
    	if(km>=14 && km<=16)
    	{
    		a = 8;
    	}
    	if(km>=16 && km<=18)
    	{
    		a = 9;
    	}
    	if(km>=18 && km<=20)
    	{
    		a = 10;
    	}
    	
		return a;
    }
}
