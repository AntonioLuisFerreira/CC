package service;

import static com.google.protobuf.ByteString.copyFrom;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


import service.*;
import service.FileServiceGrpc;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.html.ImageView;

public class ClientApp {
    //a ser alterado
    private static String svcIP = "104.197.31.205";
    //private static String svcIP = "localhost";
    private static int svcPort = 7500;
    private static ManagedChannel channel;

    //stubs
    private static FileServiceGrpc.FileServiceStub noBlockStub;
    private static FileServiceGrpc.FileServiceBlockingStub blockingStub;
    public static Map<String, String> ImageMap = new HashMap<>();

    private static int Menu(){
        Scanner scan = new Scanner(System.in);
        int option;
        do {
            System.out.println("######## MENU ##########");
            System.out.println("Options for Text App:");
            System.out.println(" 1: Enviar a Imagem");
            System.out.println(" 2: Listar as Imagens");
            System.out.println(" 3: Retornar as Imagem (original mais static)");
            System.out.println(" 4: Retornar os ids das Imagens com Probabilidade acima de 0.6");
            System.out.println("..........");
            System.out.println("0: Exit");
            System.out.print("Enter an Option:");
            option = scan.nextInt();
        } while (!((option >= 1 && option <= 4) || option == 0));
        return option;
    }



    public static void main(String[] args) throws IOException, InterruptedException {


        channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                .usePlaintext()
                .build();
        //stubs
        noBlockStub  = FileServiceGrpc.newStub(channel);
        blockingStub = FileServiceGrpc.newBlockingStub(channel);

        Scanner scan = new Scanner(System.in);

        boolean flagEnd = false;
        while(!flagEnd){
            int option = Menu();
            switch(option){
                case 0:
                    System.exit(0);
                    break;

                case 1:
                    System.out.println("Introduza o path da imagem");
                    String path = scan.nextLine();

                    sendImage(path);

                    break;

                case 2:
                    listImages();
                    break;

                case 3:
                    System.out.println("Introduza o nome da imagem que deseja processar");
                    String id = scan.nextLine();
                    getImage(id);
                    break;

                case 4:
                    getBetterScores();
                    break;
            }
        }
    }

    private static void sendImage(String path) throws IOException, InterruptedException {

        ClientImageStreamObserver clientImageStreamObserver = new ClientImageStreamObserver();
        StreamObserver<ImageRequest> requestStreamObserver = noBlockStub.sendImage(clientImageStreamObserver);

        Path uploadFicheiro = Paths.get(path);
        String contentType = Files.probeContentType(uploadFicheiro);

        byte[] image_in_bytes = Files.readAllBytes(uploadFicheiro);

        ImageRequest imageChunk = ImageRequest.newBuilder()
                .setData(ByteString.copyFrom(image_in_bytes))
                .setContentType(contentType)
                .setSize(image_in_bytes.length)
                .build();

        requestStreamObserver.onNext(imageChunk);
        requestStreamObserver.onCompleted();

        while (!clientImageStreamObserver.isCompleted()) {
            System.out.println("Imagem a ser submetida");
            Thread.sleep(1000);
        }

        List<String> imageReplyList = clientImageStreamObserver.getReplyList();
        String imageId = imageReplyList.get(0);
        ImageMap.put(imageId, path);
        System.out.println("Imagem com o id de: " + imageId);
    }

    private static void getImage(String imageId){
        ReciveImageRequest request = ReciveImageRequest.newBuilder()
                .setImageId(imageId)
                .build();

        ReciveImageReply reply = blockingStub.getImage(request);
        byte[] origImage = reply.getOriginalimage().toByteArray();
        byte[] staticImage = reply.getStaticImage().toByteArray();
        showImage("Original",origImage);
        showImage("Static Image",staticImage);


    }

    private static void listImages(){
        ListImagesRequest request = ListImagesRequest.newBuilder().build();
        ListImagesReply reply = blockingStub.listImages(request);

        List<String> idsList = new ArrayList<>(Arrays.asList(reply.getImageId().split(", ")));
        List<String> descriptionList = new ArrayList<>(Arrays.asList(reply.getDescricao().split(", ")));

        for(int i = 0; i< reply.getSize() ; i++){
            System.out.println("Imagem com Nome: " + idsList.get(i).substring(5));
            System.out.println("Imagem com Id: " + idsList.get(i));
            System.out.println("Imagem com Descrição: " + descriptionList.get(i));
            System.out.println();
        }
    }

    private static void getBetterScores(){
        SocresImageRequest request = SocresImageRequest.newBuilder().build();
        SocresImageReply reply = blockingStub.getBetterScores(request);

        List<String> idsList = new ArrayList<>(Arrays.asList(reply.getImageId().split(", ")));
        List<String> scoreList = new ArrayList<>(Arrays.asList(reply.getScore().split(", ")));
        List<String> descriptionList = new ArrayList<>(Arrays.asList(reply.getDescricao().split(", ")));

        for(int i = 0; i< reply.getSize() ; i++){
            System.out.println("Imagem com Nome: " + idsList.get(i).substring(5));
            System.out.println("Imagem com Id: " + idsList.get(i));
            System.out.println("Imagem com Descrição: " + descriptionList.get(i));
            System.out.println("Score: " + scoreList.get(i));
            System.out.println();
        }
    }

    public static void showImage(String name,byte[] imageData) {
        try {
            // Convert byte array to BufferedImage
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

            // Create a JFrame to display the image
            JFrame frame = new JFrame();
            frame.setTitle(name);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create a custom JPanel to render the image
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, null);
                }
            };

            // Set the panel size to match the image size
            panel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

            // Add the panel to the frame
            frame.getContentPane().add(panel);

            // Pack the frame to fit the panel size
            frame.pack();

            // Display the frame
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
