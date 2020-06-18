package com.l1yp.frame;

import com.l1yp.util.BaseElem;
import com.l1yp.util.BytesElem;
import com.l1yp.util.Fixed32Elem;
import com.l1yp.util.Fixed64Elem;
import com.l1yp.util.HexUtil;
import com.l1yp.util.LengthElem;
import com.l1yp.util.PbParser;
import com.l1yp.util.RootElem;
import com.l1yp.util.VarintElem;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

/**
 * @Author Lyp
 * @Date 2020/5/8
 * @Email l1yp@qq.com
 */
public class TreeForm extends JFrame {

    DefaultMutableTreeNode root;

    JTree tree;
    // JTextArea textArea;

    public TreeForm(){
        setBounds(0, 0, 1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(1, 1));
        setLocationRelativeTo(null);



        // textArea = new JTextArea();
        // textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // getContentPane().add(textArea);


        //加滚动条--------------------------------------------------------
        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane);
        //加滚动条-----------------------------------------------------

        initModel();

        tree = new JTree(root);
        tree.addKeyListener(new WinKeyListener());


        expandAll();


        CustomRender renderer = new CustomRender();
        renderer.setIcon(null);
        renderer.setDisabledIcon(null);
        renderer.setEnabled(false);
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);


        tree.setRowHeight(20);
        tree.setCellRenderer(renderer);

        scrollPane.setViewportView(tree);
    }

    private void initModel(){
        PbParser parser = new PbParser();
        byte[] bytes = HexUtil.hex2bin("08AE0D10029A01EF035000A20100F0018080808008C00200900300E2030D3130312E39312E37392E323439800550D2054536376339656332656639356230396466346465333565613163373833633336385F37323861666633322D333235352D343434622D623432652D633038663837616362666363A206B002BE965DC92FED8531C6EA206DEF18692F969A1586E827ED8028B30A400284002B9ECE5A8C6CF8805259169B8772DDECDEE639D7BCD3947DD68AF4B876FF2829F752AD8BD9BCD88D7E6474FDFA70BEA2C7C4714036890DCDE1D95FE90971874F7AB55AA1AE29CBF0528F3DFB923F7412D36BA58413A7365BDDAAFD62E917942DEFBC7003D40A72F15D5AC8DDA1487B10AD9943D5F36F3017E014D959302D37ECA6CE6F0D1D5152062CD531AFCCF6831CF89A0AD781EED18657037DAC2B95113902DF1B487F0D20694D3938B6F7D0B1C650BABA21991BE1B9A4D72670FB0167E772A7708E33DC4C309497EC2E79DD338BF4A8AC58E7E482582D7D480378BA3D27EA2DD64C6E388C49256856AE4DD557CCFABD85EF767681B877C6D2DC801EC357811BC6BAE32B5572703A8A173CB22E5615F00600C00780800892080D3130312E39312E37392E323439E008BB03B209216F662D73682D637466732D75706C6F61642D697076362E66746E2E71712E636F6DD20A0B392E39372E32332E313730");
        // root = parser.parse(null, byets, 0, byets.length);
        RootElem rootElem = parser.parse(bytes);
        System.out.println("rootElem = " + rootElem);
        root = new DefaultMutableTreeNode(rootElem);
        transferToTree(rootElem, root);
    }

    private void transferToTree(BaseElem baseElem, DefaultMutableTreeNode parentNode){
        List<BaseElem> children = baseElem.getChildren();
        for (BaseElem child : children) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child, !child.isLeaf());
            parentNode.add(node);
            if (!child.isLeaf()){
                transferToTree(child, node);
            }
        }

    }

    private void expandAll() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }


    private static class WinKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            System.out.println("keyCode = " + keyCode);
            if (keyCode == 81){
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                DataFlavor[] availableDataFlavors = clipboard.getAvailableDataFlavors();

                for (DataFlavor dataFlavor : availableDataFlavors) {
                    try {
                        System.out.println(dataFlavor.getPrimaryType());
                        System.out.println(clipboard.getData(dataFlavor));
                    } catch (UnsupportedFlavorException | IOException unsupportedFlavorException) {
                        unsupportedFlavorException.printStackTrace();
                    }
                }
            }


        }
    }

    private static class CustomRender extends DefaultTreeCellRenderer{

        private static final ImageIcon textIcon = new ImageIcon(ClassLoader.getSystemResource("letter-t.png"));
        private static final ImageIcon dwIcon = new ImageIcon(ClassLoader.getSystemResource("letter-d.png"));
        private static final ImageIcon qwIcon = new ImageIcon(ClassLoader.getSystemResource("letter-q.png"));
        private static final ImageIcon hexIcon = new ImageIcon(ClassLoader.getSystemResource("letter-h.png"));
        private static final ImageIcon vIcon = new ImageIcon(ClassLoader.getSystemResource("letter-v.png"));

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            JComponent component = (JComponent)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (leaf){
                if (value instanceof DefaultMutableTreeNode node){
                    if (node.getUserObject() instanceof VarintElem){
                        component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                        component.setForeground(Color.decode(sel ? "#ffffff" : "#000000"));
                        setIcon(vIcon);
                    }else if (node.getUserObject() instanceof LengthElem){
                        component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                        component.setForeground(Color.decode(sel ? "#ffffff" : "#5830E0"));
                    }else if (node.getUserObject() instanceof BytesElem elem){
                        component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                        Boolean printable = elem.getPrintable();
                        if (printable == null){
                            component.setForeground(Color.decode(sel ? "#ffffff" : "#bc6710"));
                            setIcon(hexIcon);
                        }else {
                            component.setForeground(Color.decode(sel ? "#ffffff" : (printable ? "#bc6710" : "#ff00ff")));
                            setIcon(printable ? textIcon : hexIcon);
                        }
                    }else if (node.getUserObject() instanceof Fixed32Elem){
                        setIcon(dwIcon);
                    }else if (node.getUserObject() instanceof Fixed64Elem){
                        setIcon(qwIcon);
                    }
                }
                component.setFont(new Font("微软雅黑", Font.BOLD, 14));
            }else {
                component.setBackground(Color.decode(sel ? "#0078d7" : "#ffffff"));
                component.setForeground(Color.decode(sel ? "#ffffff" : "#000000"));
                component.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            }


            Border border = BorderFactory.createEmptyBorder(20, 3, 20, 5);
            component.setBorder(border);
            return component;
        }
    }

}
