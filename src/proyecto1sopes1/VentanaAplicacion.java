package proyecto1sopes1;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import proyecto1sopes1.proceso;

public class VentanaAplicacion extends javax.swing.JFrame {

    public VentanaAplicacion() {
        initComponents();
    }

    public static int quantum = 0;
    public static int contadorPID = 1;
    public static ArrayList<proceso> procesosArr = new ArrayList();
    public static ArrayList<memoriaEstructura> memoriaArr = new ArrayList();
    public static boolean semaforo = true;

    public void cambiarSemaforo() {
        semaforo =! semaforo;
    }

    public void negarSemaforo() {
        semaforo = false;
    }

    public void ActualizarTabla() {
        DefaultTableModel model = (DefaultTableModel) jTableProcesos.getModel();
        model.setRowCount(0);
        for (int i = 0; i < procesosArr.size(); i++) {
            model.addRow(new Object[]{
                procesosArr.get(i).getNombre(),
                procesosArr.get(i).getUID(),
                procesosArr.get(i).getEstado(),
                procesosArr.get(i).getMemoria()
            });
        }
    }

    public void ActualizarTablaMemoria() {
        DefaultTableModel model = (DefaultTableModel) jTableMemoria.getModel();
        model.setRowCount(0);
        for (int i = 0; i < memoriaArr.size(); i++) {
            model.addRow(new Object[]{
                memoriaArr.get(i).getTamano(),
                memoriaArr.get(i).getUso(),
                memoriaArr.get(i).getDisponible(),
                memoriaArr.get(i).getListaProcesosInternos()
            });
        }
    }

    void Asignar(proceso proceso) {
        try {
            if (memoriaArr.size() < 1 && proceso.getMemoria() <= 200000) {
                memoriaEstructura aux = new memoriaEstructura();
                aux.agregarProcesoAMemoria(proceso);
                aux.setUID(proceso.getUID());
                memoriaArr.add(aux);
            } else if (proceso.getMemoria() > 200000) {
                proceso copiaproceso_uno = proceso.copia(proceso);
                proceso copiaproceso_dos = proceso.copia(proceso);

                copiaproceso_uno.setMemoria(copiaproceso_dos.getMemoria() - 200000);

                memoriaEstructura aux = new memoriaEstructura();
                aux.agregarProcesoAMemoria(copiaproceso_dos);
                aux.setUID(copiaproceso_dos.getUID());
                memoriaArr.add(aux);
                memoriaArr.get(memoriaArr.size() - 1).setearUltimaMemoria();

                memoriaEstructura aux2 = new memoriaEstructura();
                aux2.agregarProcesoAMemoria(copiaproceso_uno);
                aux2.setUID(copiaproceso_uno.getUID());
                memoriaArr.add(aux2);

            } else if (memoriaArr.get(memoriaArr.size() - 1).getDisponible() - proceso.getMemoria() > 0) {
                memoriaArr.get(memoriaArr.size() - 1).agregarProcesoAMemoria(proceso);
            } else {
                memoriaEstructura aux = new memoriaEstructura();
                aux.agregarProcesoAMemoria(proceso);
                aux.setUID(proceso.getUID());
                memoriaArr.add(aux);
            }
        } catch (Exception e) {
            System.out.println("Error al asignar");
        }
        ActualizarTablaMemoria();
    }

    void arrancarPlanificador() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < procesosArr.size(); i++) {
                        if (procesosArr.get(i).getTimerInternoParaAsignarAMemoria() > 0) {
                            procesosArr.get(i).disminuirTimer();
                            if (procesosArr.get(i).getTimerInternoParaAsignarAMemoria() <= 0) {
                                procesosArr.get(i).setEstado("Asignado");
                                negarSemaforo();
                                Asignar(procesosArr.get(i));
                                ActualizarTabla();
                            }
                        }
                    }

                    if (semaforo) {
                        if (memoriaArr.size() < 1) {
                        } else {
                            for (int i = 0; i < memoriaArr.size(); i++) {
                                if (procesosArr.get(0).getEstado().equals("Asignado")) {
                                    for (int j = 0; j < memoriaArr.get(i).procesosInternos.size(); j++) {
                                        if (memoriaArr.get(i).procesosInternos.get(j).getUID() == procesosArr.get(0).getUID()) {
                                            memoriaArr.get(i).procesosInternos.remove(j);
                                        }
                                    }
                                   
                                    if (memoriaArr.get(i).getDisponible() > 199999) { 
                                        memoriaArr.remove(i);
                                    }
                                } else {
                                    System.out.println("aun no estaba asignado el proceso " + procesosArr.get(0).getNombre());
                                }
                            }
                            for (int i = 0; i < memoriaArr.size(); i++) {
                                if (procesosArr.get(0).getEstado().equals("Asignado")) {
                                    for (int j = 0; j < memoriaArr.get(i).procesosInternos.size(); j++) {
                                        if (memoriaArr.get(i).procesosInternos.get(j).getUID() == procesosArr.get(0).getUID()) {
                                            memoriaArr.get(i).procesosInternos.remove(j);
                                        }
                                    }
                                   
                                    if (memoriaArr.get(i).getDisponible() > 199999) { 
                                        memoriaArr.remove(i);
                                    }
                                } else {
                                    System.out.println("aun no estaba asignado el proceso " + procesosArr.get(0).getNombre());
                                }
                            }
                            procesosArr.remove(0);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error en el planificador!");
                    System.out.println(e);
                } finally {
                    ActualizarTabla();
                    ActualizarTablaMemoria();
                    cambiarSemaforo();
                }

            }

        }, 0, quantum);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableMemoria = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProcesos = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabelQuantum = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButtonAñadir = new javax.swing.JButton();
        jTextFieldMemoria = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 255, 0));
        setName("SEGUNDO PROYECTO"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("Sistemas Operativos - Entrega No.2");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("Victor Guerra 7691-19-11984");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTableMemoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Memoria (KB)", "En uso (KB)", "Disponible (KB)", "Procesos"
            }
        ));
        jTableMemoria.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jTableMemoria.setEnabled(false);
        jScrollPane2.setViewportView(jTableMemoria);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Memoria en uso");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(153, 255, 153));

        jTableProcesos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Proceso", "PID", "Estado", "Memoria (KB)"
            }
        ));
        jTableProcesos.setEnabled(false);
        jScrollPane1.setViewportView(jTableProcesos);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Lista de Procesos");

        jLabelQuantum.setText("1000 - 10000");

        jLabel6.setText("ms");

        jLabel5.setText("Quantum:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(343, 343, 343)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelQuantum)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabelQuantum)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jButtonAñadir.setBackground(new java.awt.Color(204, 204, 204));
        jButtonAñadir.setForeground(new java.awt.Color(255, 0, 51));
        jButtonAñadir.setText("Añadir");
        jButtonAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAñadirActionPerformed(evt);
            }
        });

        jTextFieldMemoria.setText("0");
        jTextFieldMemoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMemoriaActionPerformed(evt);
            }
        });

        jLabel7.setText("Nombre");

        jLabel8.setText("Memoria");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Agregar a Cola");

        jLabel12.setText("KB");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldMemoria, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(33, 33, 33)
                                .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(jButtonAñadir)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAñadir)
                        .addGap(8, 8, 8)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldMemoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addComponent(jLabel8))
                .addGap(18, 18, 18))
        );

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 153, 153));
        jLabel11.setText("Edgar Tellez 7691-15-9407");

        jLabel10.setText("Funcional");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1)
                                .addGap(278, 278, 278))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addGap(16, 16, 16))))))
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldMemoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMemoriaActionPerformed

    }//GEN-LAST:event_jTextFieldMemoriaActionPerformed


    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        int min = 1000;
        int max = 10000;

        quantum = (int) Math.floor(Math.random() * (max - min + 1) + min);

        jLabelQuantum.setText(Integer.toString(quantum));
        ActualizarTabla();
        arrancarPlanificador();
    }//GEN-LAST:event_formWindowOpened

    private void jButtonAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAñadirActionPerformed
        try {
            if (Integer.parseInt(jTextFieldMemoria.getText()) > 400000 || Integer.parseInt(jTextFieldMemoria.getText()) < 1) {
                JOptionPane.showMessageDialog(new JFrame(), "No puede superar 400,000kb, segun instrucciones del proyecto No.2");
            } else if (jTextFieldNombre.getText().equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "El proceso debe contener un nombre");
            } else if (procesosArr.size() < 15) {
                proceso aux = new proceso();
                aux.setNombre(jTextFieldNombre.getText() + ".exe");
                aux.setUID(contadorPID++);
                aux.setEstado("En espera");
                aux.setMemoria(Integer.parseInt(jTextFieldMemoria.getText()));
                procesosArr.add(aux);
                ActualizarTabla();
                jTextFieldMemoria.setText("0");
                jTextFieldNombre.setText("");
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "No se puede crear mas de 15 proceso, hay que considerar la memoria");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "El proceso debe tener un tamaño mayor a 0kb y menor a 400,000kb");
            jTextFieldMemoria.setText("0");
        }


    }//GEN-LAST:event_jButtonAñadirActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaAplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaAplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaAplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaAplicacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaAplicacion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAñadir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelQuantum;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableMemoria;
    private javax.swing.JTable jTableProcesos;
    private javax.swing.JTextField jTextFieldMemoria;
    private javax.swing.JTextField jTextFieldNombre;
    // End of variables declaration//GEN-END:variables
}
